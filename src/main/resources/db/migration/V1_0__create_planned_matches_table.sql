CREATE TABLE planned_matches (
  id INTEGER NOT NULL UNIQUE PRIMARY KEY,
  match_time TIMESTAMP WITH TIME ZONE,
  arena VARCHAR(200),
  home_team VARCHAR(200),
  away_team VARCHAR(200),
  home_score INTEGER,
  away_score INTEGER,
  fulltime BOOLEAN,
  matchType VARCHAR(15)
);

CREATE TABLE players (
  id SERIAL PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  salt varchar(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  paid BOOLEAN DEFAULT FALSE,
  created TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE player_guesses (
  match_id INTEGER REFERENCES planned_matches(id) ON DELETE CASCADE,
  player_id INTEGER REFERENCES players(id) ON DELETE CASCADE,
  homes_score INTEGER,
  away_score INTEGER
);