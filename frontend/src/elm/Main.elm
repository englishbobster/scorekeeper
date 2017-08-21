module ScoreKeeper exposing (..)

import Html exposing (Html, program, div, table, thead, tbody, th, tr, td, text)
import Http exposing (Request)
import Html.Attributes exposing (class)
import Json.Decode exposing (Decoder, at, list, int, string, bool, map2, map8, field)


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
    { id : Int
    , homeTeam : String
    , awayTeam : String
    , score : Score
    , matchTime : String
    , arena : String
    , matchType : String
    , fullTime : Bool
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
                Ok fetchedMatches ->
                    ( { model | listMatches = fetchedMatches }, Cmd.none )

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
    map8 PlannedMatch
        (field "id" int)
        (field "homeTeam" string)
        (field "awayTeam" string)
        (at [ "score" ] scoreDecoder)
        (field "matchTime" string)
        (field "arena" string)
        (field "matchType" string)
        (field "fullTime" bool)



--View


view : Model -> Html msg
view model =
    div [] [ errorOrView model ]


errorOrView : Model -> Html msg
errorOrView model =
    case model.loadingError of
        Just error ->
            text (toString error)

        Nothing ->
            makeFootballMatchTable model.listMatches


makeFootballMatchTable : List PlannedMatch -> Html msg
makeFootballMatchTable listOfMatches =
    div [ class "datagrid" ]
        [ table
            [ class "datagrid" ]
            [ makeFootballMatchHeader
            , tbody [ class "datagrid" ] (List.map (\match -> makeFootballMatchRow match) listOfMatches)
            ]
        ]


makeFootballMatchHeader : Html msg
makeFootballMatchHeader =
    thead [ class "datagrid" ]
        [ th [] [ text "Match #" ]
        , th [] [ text "Home Team" ]
        , th [] [ text "Home Score" ]
        , th [] [ text "Away Score" ]
        , th [] [ text "Away Team" ]
        , th [] [ text "Date" ]
        , th [] [ text "Arena" ]
        , th [] [ text "Group/Round" ]
        , th [] [ text "Full Time" ]
        ]


makeFootballMatchRow : PlannedMatch -> Html msg
makeFootballMatchRow match =
    tr []
        [ td [] [ text (toString match.id) ]
        , td [] [ text match.homeTeam ]
        , td [] [ text (toString match.score.homeScore) ]
        , td [] [ text (toString match.score.awayScore) ]
        , td [] [ text match.awayTeam ]
        , td [] [ text match.matchTime ]
        , td [] [ text match.arena ]
        , td [] [ text match.matchType ]
        , td [] [ text (toString match.fullTime) ]
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
