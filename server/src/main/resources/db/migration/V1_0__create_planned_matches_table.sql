CREATE TABLE planned_matches (
  id INTEGER NOT NULL PRIMARY KEY,
  match_time TIMESTAMP,
  arena VARCHAR(200),
  home_team VARCHAR(200),
  away_team VARCHAR(200),
  home_score INTEGER,
  away_score INTEGER,
  fulltime BOOLEAN,
  matchType VARCHAR(15),
  group_id VARCHAR(2)
);
