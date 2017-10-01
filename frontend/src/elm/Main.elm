port module ScoreKeeper exposing (..)

import Html exposing (Html, program, text, div, form, h2, p, label, input, button)
import Html.Attributes exposing (class, classList, id, for, type_, value)
import Html.Events exposing (onInput, onClick)
import Http exposing (Request, send, Body, post, jsonBody)
import Json.Encode as Enc exposing (object, string, encode)
import Json.Decode as Dec exposing (Decoder, map2, string, int, field)
import Task exposing (perform)
import Time exposing (Time, now)
import Time.TimeZones as TimeZones exposing (europe_stockholm)
import Time.ZonedDateTime as ZonedDateTime exposing (ZonedDateTime, fromTimestamp, toISO8601)
import Navigation exposing (modifyUrl, load)


-- Model


type alias Model =
    { username : String
    , password : String
    , email : String
    , created : String
    , id : Int
    , token : String
    , errorMsg : String
    }


initialModel : Model
initialModel =
    { username = ""
    , password = ""
    , email = ""
    , created = ""
    , id = 0
    , token = ""
    , errorMsg = ""
    }


type Msg
    = SetUserName String
    | SetPassword String
    | SetEmail String
    | SetCreated Time
    | RegisterClick
    | RegisterPlayer (Result Http.Error { id : Int, token : String })



-- Update


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        SetUserName name ->
            ( { model | username = name }, Cmd.none )

        SetPassword pwd ->
            ( { model | password = pwd }, Cmd.none )

        SetEmail address ->
            ( { model | email = address }, Cmd.none )

        RegisterClick ->
            ( model, timeNow )

        SetCreated time ->
            ( { model | created = timeAsISOString time }, registerPlayer model (timeAsISOString time) )

        RegisterPlayer (Ok credentials) ->
            ( { model | id = credentials.id, token = credentials.token }, Cmd.batch [ storeSession model, load "http://127.0.0.1:5000/test.html" ] )

        RegisterPlayer (Err httpErr) ->
            ( { model | errorMsg = toString (httpErr) }, Cmd.none )


timeNow : Cmd Msg
timeNow =
    Task.perform SetCreated Time.now


timeAsISOString : Time -> String
timeAsISOString time =
    (fromTimestamp (europe_stockholm ()) time) |> toISO8601



-- Ports


port storeSession : Model -> Cmd msg



-- API calls


registerPlayer : Model -> String -> Cmd Msg
registerPlayer model time =
    send RegisterPlayer (registerRequest model time)


registerRequest : { r | username : String, password : String, email : String } -> String -> Request { id : Int, token : String }
registerRequest { username, password, email } time =
    let
        body =
            object
                [ ( "username", Enc.string username )
                , ( "password", Enc.string password )
                , ( "email", Enc.string email )
                , ( "created", Enc.string time )
                ]
                |> jsonBody

        replyDecoder =
            map2 (\a b -> { id = a, token = b })
                (field "id" int)
                (field "token" Dec.string)
    in
        post "http://127.0.0.1:5000/register" body replyDecoder



-- View


view : Model -> Html Msg
view model =
    div [ class "container" ]
        [ div [ class "jumbotron text-left" ]
            [ div [ id "form" ]
                [ h2 [ class "text-center" ] [ text "Register" ]
                , p [ class "help-block text-center" ] [ text "Register a player to compete. The user name must be unique." ]
                , div [ classList [ ( "invisible", model.errorMsg == "" ) ] ]
                    [ div [ class "alert alert-danger" ] [ text model.errorMsg ] ]
                , fieldInput "Username" "username" model.username SetUserName
                , fieldInput "Password" "password" model.password SetPassword
                , fieldInput "Email" "email" model.email SetEmail
                , div [ class "text-center" ]
                    [ button [ class "btn btn-primary", onClick RegisterClick ] [ text "Register" ]
                    ]
                ]
            ]
        , div [] [ text (toString model) ]
        ]


fieldInput : String -> String -> String -> (String -> Msg) -> Html Msg
fieldInput lab identity val msg =
    div [ class "form-group row" ]
        [ div [ class "col-md-offset-2 col-md-8" ]
            [ label [ for identity ] [ text (lab ++ ":") ]
            , input [ id identity, type_ "text", class "form-control", value val, onInput msg ] []
            ]
        ]



-- Main


main : Program Never Model Msg
main =
    program
        { init = ( initialModel, Cmd.none )
        , view = view
        , update = update
        , subscriptions = (\model -> Sub.none)
        }
