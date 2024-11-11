-- ------------------------------
-- OPTION
-- ------------------------------

OPTION IMPORT;

-- ------------------------------
-- FUNCTIONS
-- ------------------------------

DEFINE FUNCTION fn::add_programme($name: string, $path: string, $hash: string) {
LET $result = (UPSERT programme SET name = $name, path = $path, hash = $hash);
LET $program = $result[0].id;
RELATE $program -> pinTo -> mime:downloading;
CREATE music SET title = '', artist = '', type = 'OP 1', belongsTo = $program;
CREATE music SET title = '', artist = '', type = 'ED 1', belongsTo = $program;
} PERMISSIONS FULL;
DEFINE FUNCTION fn::init() {
UPSERT mime:downloading SET name = 'Downloading';
UPSERT mime:watching SET name = 'Watching';
UPSERT mime:favorites SET name = 'Favorites';
} PERMISSIONS FULL;
DEFINE FUNCTION fn::reset() {
DELETE jade;
DELETE music;
DELETE programme;
} PERMISSIONS FULL;

-- ------------------------------
-- TABLE: bucket
-- ------------------------------

DEFINE TABLE bucket TYPE NORMAL SCHEMALESS PERMISSIONS NONE;

DEFINE FIELD name ON bucket TYPE string PERMISSIONS FULL;



-- ------------------------------
-- TABLE DATA: bucket
-- ------------------------------


-- ------------------------------
-- TABLE: comments
-- ------------------------------

DEFINE TABLE comments TYPE RELATION IN user OUT programme | music | episode SCHEMALESS PERMISSIONS NONE;

DEFINE FIELD comment ON comments TYPE string PERMISSIONS FULL;
DEFINE FIELD in ON comments TYPE record<user> PERMISSIONS FULL;
DEFINE FIELD out ON comments TYPE record<programme | music | episode> PERMISSIONS FULL;



-- ------------------------------
-- TABLE DATA: comments
-- ------------------------------


-- ------------------------------
-- TABLE: episode
-- ------------------------------

DEFINE TABLE episode TYPE NORMAL SCHEMALESS PERMISSIONS NONE;

DEFINE FIELD name ON episode TYPE string PERMISSIONS FULL;
DEFINE FIELD path ON episode TYPE string PERMISSIONS FULL;



-- ------------------------------
-- TABLE DATA: episode
-- ------------------------------


-- ------------------------------
-- TABLE: favorites
-- ------------------------------

DEFINE TABLE favorites TYPE RELATION IN user OUT programme | music | episode SCHEMALESS PERMISSIONS NONE;

DEFINE FIELD in ON favorites TYPE record<user> PERMISSIONS FULL;
DEFINE FIELD out ON favorites TYPE record<programme | music | episode> PERMISSIONS FULL;



-- ------------------------------
-- TABLE DATA: favorites
-- ------------------------------


-- ------------------------------
-- TABLE: music
-- ------------------------------

DEFINE TABLE music TYPE NORMAL SCHEMALESS PERMISSIONS NONE;

DEFINE FIELD artist ON music PERMISSIONS FULL;
DEFINE FIELD title ON music TYPE string PERMISSIONS FULL;
DEFINE FIELD type ON music TYPE string PERMISSIONS FULL;



-- ------------------------------
-- TABLE DATA: music
-- ------------------------------

INSERT [ { artist: '', belongsTo: programme:hhtppnqjq7a2j081drr4, id: music:b5y95uqorus1nbt4ldc2, title: '', type: 'ED 1' }, { artist: '', belongsTo: programme:hhtppnqjq7a2j081drr4, id: music:w4o84mshit0q8ei18zyu, title: '', type: 'OP 1' } ];

-- ------------------------------
-- TABLE: pinTo
-- ------------------------------

DEFINE TABLE pinTo TYPE RELATION IN programme OUT bucket SCHEMALESS PERMISSIONS NONE;

DEFINE FIELD in ON pinTo TYPE record<programme> PERMISSIONS FULL;
DEFINE FIELD out ON pinTo TYPE record<bucket> PERMISSIONS FULL;



-- ------------------------------
-- TABLE DATA: pinTo
-- ------------------------------

INSERT RELATION [ { __: true, id: pinTo:bwjyklupeyg5jshaftfm, in: programme:hhtppnqjq7a2j081drr4, out: mime:downloading } ];

-- ------------------------------
-- TABLE: programme
-- ------------------------------

DEFINE TABLE programme TYPE NORMAL SCHEMALESS PERMISSIONS NONE;

DEFINE FIELD name ON programme TYPE string PERMISSIONS FULL;
DEFINE FIELD path ON programme TYPE string PERMISSIONS FULL;



-- ------------------------------
-- TABLE DATA: programme
-- ------------------------------

INSERT [ { hash: 'robin:2024-11-28T22:50:45.503016Z', id: programme:hhtppnqjq7a2j081drr4, name: 'Chobits', path: '/path/to/chobits' } ];

-- ------------------------------
-- TABLE: user
-- ------------------------------

DEFINE TABLE user TYPE NORMAL SCHEMALESS PERMISSIONS NONE;

DEFINE FIELD firstName ON user TYPE string PERMISSIONS FULL;
DEFINE FIELD lastName ON user TYPE string PERMISSIONS FULL;



-- ------------------------------
-- TABLE DATA: user
-- ------------------------------


-- ------------------------------
-- TABLE: watches
-- ------------------------------

DEFINE TABLE watches TYPE RELATION IN user OUT episode SCHEMALESS PERMISSIONS NONE;

DEFINE FIELD in ON watches TYPE record<user> PERMISSIONS FULL;
DEFINE FIELD on ON watches TYPE datetime PERMISSIONS FULL;
DEFINE FIELD out ON watches TYPE record<episode> PERMISSIONS FULL;
DEFINE FIELD timestamp ON watches TYPE string PERMISSIONS FULL;



-- ------------------------------
-- TABLE DATA: watches
-- ------------------------------


