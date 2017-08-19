module ScoreKeeper exposing (..)

import Html exposing (Html, program, div, table, tr, td, text)
import Http exposing (Request)
import Json.Decode exposing (Decoder, at, list, int, string, map2, map6, field)


--Constants


type alias Constants =
    { backendAddress : String
    , backendPort : Int
    , plannedMatchesPath : String
    }


constants : Constants
constants =
    { backendAddress = "127.0.0.1"
    , backendPort = 4567
    , plannedMatchesPath = "/plannedmatches"
    }



--Model


type alias Score =
    { homeScore : Int
    , awayScore : Int
    }


type alias PlannedMatch =
    { homeTeam : String
    , awayTeam : String
    , score : Score
    , matchTime : String
    , arena : String
    , matchType : String
    }


type alias Model =
    { listMatches : List PlannedMatch
    , loadingError : Maybe String
    }


initialModel : Model
initialModel =
    { listMatches = []
    , loadingError = Nothing
    }



--Update


type Msg
    = NoOp
    | FetchPlannedMatches (Result Http.Error (List PlannedMatch))


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        NoOp ->
            ( model, Cmd.none )

        FetchPlannedMatches result ->
            case result of
                Ok responseStr ->
                    ( { model | listMatches = responseStr }, Cmd.none )

                Err httpError ->
                    ( { model | loadingError = Just (toString httpError) }, Cmd.none )


getPlannedMatches : Cmd Msg
getPlannedMatches =
    list footballMatchDecoder
        |> Http.get plannedMatchesUrl
        |> Http.send FetchPlannedMatches


plannedMatchesUrl : String
plannedMatchesUrl =
    "http://"
        ++ constants.backendAddress
        ++ ":"
        ++ toString (constants.backendPort)
        ++ constants.plannedMatchesPath


scoreDecoder : Decoder Score
scoreDecoder =
    map2 Score
        (field "homeScore" int)
        (field "awayScore" int)


footballMatchDecoder : Decoder PlannedMatch
footballMatchDecoder =
    map6 PlannedMatch
        (field "homeTeam" string)
        (field "awayTeam" string)
        (at [ "score" ] scoreDecoder)
        (field "matchTime" string)
        (field "arena" string)
        (field "matchType" string)



--View


view : Model -> Html msg
view model =
    div []
        [ text (toString model.loadingError)
        , table [] (List.map (\match -> makeFootballMatchRow match) model.listMatches)
        ]


makeFootballMatchRow : PlannedMatch -> Html msg
makeFootballMatchRow match =
    tr []
        [ td [] [ text match.homeTeam ]
        , td [] [ text (toString match.score.homeScore) ]
        , td [] [ text (toString match.score.awayScore) ]
        , td [] [ text match.awayTeam ]
        , td [] [ text match.matchTime ]
        , td [] [ text match.arena ]
        , td [] [ text match.matchType ]
        ]



--Main


main : Program Never Model Msg
main =
    program
        { init = ( initialModel, getPlannedMatches )
        , view = view
        , update = update
        , subscriptions = (\model -> Sub.none)
        }
