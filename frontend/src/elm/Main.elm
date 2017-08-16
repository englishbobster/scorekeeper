module ScoreKeeper exposing (..)

import Html exposing (Html, program, table, tr, td, text)
import Http exposing (Request)
import Json.Decode exposing (Decoder, list, int, string, map7, field)


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


type Group
    = A
    | B
    | C
    | D
    | E
    | F
    | G


type MatchType
    = GroupGame Group
    | R16
    | QF
    | SF
    | ThirdPlace
    | Final


stringToMatchType : String -> MatchType
stringToMatchType typeString =
    Final


type alias PlannedMatch =
    { homeTeam : String
    , homeScore : Int
    , awayTeam : String
    , awayScore : Int
    , matchTime : String
    , arena : String
    , matchType : MatchType
    }


type alias Model =
    List PlannedMatch


initialModel : Model
initialModel =
    []



--Update


type Msg
    = NoOp
    | FetchPlannedMatches (Result Http.Error Model)


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        NoOp ->
            ( model, Cmd.none )

        FetchPlannedMatches result ->
            case result of
                Ok responseStr ->
                    ( responseStr, Cmd.none )

                Err httpError ->
                    ( model, Cmd.none )


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


footballMatchDecoder : Decoder PlannedMatch
footballMatchDecoder =
    map7
        (\ht hsc at asc mt ar mty ->
            { homeTeam = ht
            , homeScore = hsc
            , awayTeam = at
            , awayScore = asc
            , matchTime = mt
            , arena = ar
            , matchType = (stringToMatchType mty)
            }
        )
        (field "homeTeam" string)
        (field "homeScore" int)
        (field "awayTeam" string)
        (field "awayScore" int)
        (field "matchTime" string)
        (field "arena" string)
        (field "matchType" string)



--View


view : Model -> Html msg
view model =
    table [] (List.map (\match -> makeFootballMatchRow match) model)


makeFootballMatchRow : PlannedMatch -> Html msg
makeFootballMatchRow match =
    tr []
        [ td [] [ text match.homeTeam ]
        , td [] [ text (toString match.homeScore) ]
        , td [] [ text (toString match.awayScore) ]
        , td [] [ text match.awayTeam ]
        , td [] [ text match.matchTime ]
        , td [] [ text match.arena ]
        , td [] [ text (toString match.matchType) ]
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
