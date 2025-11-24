INSERT INTO games(title, genre, rating, release_year)
VALUES
    ("The Game", "Horror", 10, "2010"),
     ("Mississippi River", "Adventure", 8, "2020");

INSERT INTO reviews(review, game_id) VALUES
    ("A master pice!", 1),
    ("Not bad.", 1),
    ("Don't see it at all.", 2);

INSERT INTO roles(name) VALUES ('ROLE_USER');
INSERT INTO roles(name) VALUES ('ROLE_ADMIN');

INSERT INTO users(username, password) VALUES ('test', '$2a$12$VAbgw3A6dpd75tEUudrqXOlelyuqecc54N9WNfxN7zStPQV7aY8EO'); -- 1234
INSERT INTO users(username, password) VALUES ('admin', '$2a$12$CXqSeBneszgxnW96G8KDZuk44Px5KeBTN3BHGnZKqv1jv8nkZBrTC'); -- admin123
INSERT INTO users(username, password) VALUES ('user', '$2a$12$3/wHU3SprT/iVt1h7H6UqODBP4ASa2AOfgOGO.NdvuMz6MIcDSi5.'); -- user123

INSERT INTO user_roles(user_id, role_id) VALUES (1, 1); -- test → USER
INSERT INTO user_roles(user_id, role_id) VALUES (2, 1); -- admin → USER
INSERT INTO user_roles(user_id, role_id) VALUES (2, 2); -- admin → ADMIN