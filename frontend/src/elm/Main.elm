module ScoreKeeper exposing (..)

import Html exposing (Html, text, div, form, h2, p, label, input, button)
import Html.Attributes exposing (class, classList, id, for, type_, value)
import Html.Events exposing (onInput)


-- Model


type alias Model =
    { username : String
    , password : String
    , email : String
    , id : Int
    , token : String
    , errorMsg : String
    }


initialModel : Model
initialModel =
    { username = ""
    , password = ""
    , email = ""
    , id = 0
    , token = ""
    , errorMsg = ""
    }


type Msg
    = SetUserName String
    | SetPassword String
    | SetEmail String



-- Update


update : Model -> Msg -> ( Model, Cmd msg )
update model msg =
    case msg of
        SetUserName name ->
            ( { model | username = name }, Cmd.none )

        SetPassword pwd ->
            ( { model | password = pwd }, Cmd.none )

        SetEmail address ->
            ( { model | email = address }, Cmd.none )



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
                    [ button [ class "btn btn-primary" ] [ text "Register" ]
                    ]
                ]
            ]
        ]


fieldInput : String -> String -> String -> (String -> Msg) -> Html Msg
fieldInput lab identity val msg =
    div [ class "form-group row" ]
        [ div [ class "col-md-offset-2 col-md-8" ]
            [ label [ for identity ] [ text (lab ++ ":") ]
            , input [ id identity, type_ "text", class "form-control", value val, onInput msg ] []
            ]
        ]



--Main


main : Html Msg
main =
    view initialModel
