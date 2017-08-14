module ScoreKeeper exposing (..)

import Html exposing (Html, table, tr, td, text)


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


type alias PlannedMatch =
    { homeTeam : String
    , homeScore : Int
    , awayTeam : String
    , awayScore : Int
    , matchTime : String
    , arena : String
    , matchType : MatchType
    }


type alias PlannedMatches =
    List PlannedMatch


type alias Model =
    PlannedMatches


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
    ]



--View


view : Model -> Html msg
view model =
    table [] (List.map (\match -> makeMatchRow match) model)


makeMatchRow : PlannedMatch -> Html msg
makeMatchRow match =
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


main =
    view initialModel
