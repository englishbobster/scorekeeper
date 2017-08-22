module ScoreKeeper exposing (..)

import Html exposing (Html, program, input, div, table, thead, tbody, th, tr, td, text, label)
import Html.Attributes exposing (class, type_)
import Html.Events exposing (onClick)
import Http exposing (Request)
import Json.Decode exposing (Decoder, at, list, int, string, bool, map2, map8, field)
import Array exposing (fromList, toList)


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


type alias MatchId =
    Int


type alias PlannedMatch =
    { id : MatchId
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
    | ToggleFullTime MatchId


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        NoOp ->
            ( model, Cmd.none )

        ToggleFullTime matchId ->
            ( { model | listMatches = (toggleFullTimeForMatchId matchId model.listMatches) }, Cmd.none )

        FetchPlannedMatches result ->
            case result of
                Ok fetchedMatches ->
                    ( { model | listMatches = fetchedMatches }, Cmd.none )

                Err httpError ->
                    ( { model | loadingError = Just (toString httpError) }, Cmd.none )


toggleFullTimeForMatchId : MatchId -> List PlannedMatch -> List PlannedMatch
toggleFullTimeForMatchId id plannedMatches =
    let
        matchArray =
            Array.fromList plannedMatches
    in
        Array.toList matchArray


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


view : Model -> Html Msg
view model =
    div [] [ errorOrView model ]


errorOrView : Model -> Html Msg
errorOrView model =
    case model.loadingError of
        Just error ->
            text (toString error)

        Nothing ->
            makeFootballMatchTable model.listMatches


makeFootballMatchTable : List PlannedMatch -> Html Msg
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


makeFootballMatchRow : PlannedMatch -> Html Msg
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
        , td [] [ checkbox (ToggleFullTime match.id) ]
        ]


checkbox : msg -> Html msg
checkbox msg =
    input [ type_ "checkbox", onClick msg ] []



--Main


main : Program Never Model Msg
main =
    program
        { init = ( initialModel, getPlannedMatches )
        , view = view
        , update = update
        , subscriptions = (\model -> Sub.none)
        }
