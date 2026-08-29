CREATE TABLE grid (
    grid_id     INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rows_grid   INT NOT NULL,
    cols_grid   INT NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE cell_types AS ENUM ('WALL', 'START', 'END');

CREATE TABLE grid_cell (
    cell_id     INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    grid_id     INT NOT NULL REFERENCES grid(grid_id) ON DELETE CASCADE,
    row_index   INT NOT NULL,
    col_index   INT NOT NULL,
    cell_type   cell_types NOT NULL,
    UNIQUE (grid_id, row_index, col_index)
);

CREATE TYPE algorithms AS ENUM ('BFS', 'DFS', 'DIJKSTRA', 'ASTAR');

CREATE TABLE algo_run (
    run_id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    grid_id         INT NOT NULL REFERENCES grid(grid_id) ON DELETE CASCADE,
    algorithm       algorithms NOT NULL,
    execution_time  DOUBLE PRECISION NOT NULL,
    cells_explored  INT NOT NULL,
    path_length     INT NOT NULL
);

CREATE TYPE cell_states AS ENUM ('EXPLORED', 'PATH');

CREATE TABLE path_cell (
    path_cell_id    INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    run_id          INT NOT NULL REFERENCES algo_run(run_id) ON DELETE CASCADE,
    row_index       INT NOT NULL,
    col_index       INT NOT NULL,
    cell_state      cell_states NOT NULL
);