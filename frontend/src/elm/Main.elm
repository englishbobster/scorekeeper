module ScoreKeeper exposing (..)

import Html exposing (Html, program, input, div, table, thead, tbody, th, tr, td, text, label, h1)
import Html.Attributes exposing (class, type_, checked)
import Html.Events exposing (onClick)
import Http exposing (Request)
import Json.Decode exposing (Decoder, at, list, int, string, bool, map2, map8, field)
import Dict exposing (Dict)


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


type alias DictIdToMatches =
    Dict MatchId PlannedMatch


type alias Model =
    { matches : DictIdToMatches
    , loadingError : Maybe String
    }


initialModel : Model
initialModel =
    { matches = Dict.empty
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
            ( { model | matches = (toggleFullTimeForMatchId matchId model.matches) }, Cmd.none )

        FetchPlannedMatches result ->
            case result of
                Ok fetchedMatches ->
                    ( { model | matches = listMatchesToDict fetchedMatches }, Cmd.none )

                Err httpError ->
                    ( { model | loadingError = Just (toString httpError) }, Cmd.none )


toggleFullTimeForMatchId : MatchId -> DictIdToMatches -> DictIdToMatches
toggleFullTimeForMatchId id plannedMatches =
    Dict.update id (toggleFullTime) plannedMatches


toggleFullTime : Maybe PlannedMatch -> Maybe PlannedMatch
toggleFullTime maybeMatch =
    case maybeMatch of
        Just match ->
            Just ({ match | fullTime = not match.fullTime })

        Nothing ->
            maybeMatch


listMatchesToDict : List PlannedMatch -> DictIdToMatches
listMatchesToDict matchList =
    List.map (\match -> ( match.id, match )) matchList
        |> Dict.fromList


listMatchesFromDict : DictIdToMatches -> List PlannedMatch
listMatchesFromDict matchDict =
    Dict.values matchDict


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
            makeFootballMatchTable (listMatchesFromDict model.matches)


makeFootballMatchTable : List PlannedMatch -> Html Msg
makeFootballMatchTable listOfMatches =
    div [ class "datagrid" ]
        [ table
            [ class "datagrid" ]
            [ makeFootballMatchHeader
            , tbody [ class "datagrid" ] (List.map (\match -> makeFootballMatchRow match) listOfMatches)
            ]
        , h1 [] [ text "debug info" ]
        , div [] [ text (toString listOfMatches) ]
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
        , td [] [ checkbox (ToggleFullTime match.id) match.fullTime ]
        ]


checkbox : msg -> Bool -> Html msg
checkbox msg check =
    input [ checked check, type_ "checkbox", onClick msg ] []



--Main


main : Program Never Model Msg
main =
    program
        { init = ( initialModel, getPlannedMatches )
        , view = view
        , update = update
        , subscriptions = (\model -> Sub.none)
        }
