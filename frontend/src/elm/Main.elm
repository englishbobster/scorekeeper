module ScoreKeeper exposing (..)

import Html exposing (Html, program, table, tr, td, text)
import Http exposing (Request)
import Json.Decode exposing (Decoder, list, int, string, map7, field)


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
    [ { homeTeam = "Brazil"
      , homeScore = 1
      , awayTeam = "Croatia"
      , awayScore = 0
      , matchTime = "fix a timestamp"
      , arena = "amazoa"
      , matchType = GroupGame A
      }
    , { homeTeam = "Sweden"
      , homeScore = 3
      , awayTeam = "England"
      , awayScore = 3
      , matchTime = "another timestamp"
      , arena = "sao paulo"
      , matchType = Final
      }
    , PlannedMatch "Italy" 3 "France" 2 "timestamp" "rio" (GroupGame B)
    ]



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
        |> Http.get "http://127.0.0.1:4567/plannedmatches"
        |> Http.send FetchPlannedMatches


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
