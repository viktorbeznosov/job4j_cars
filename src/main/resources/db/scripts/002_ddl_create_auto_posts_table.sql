CREATE TABLE auto_post (
   id SERIAL PRIMARY KEY,
   description TEXT NOT NULL,
   created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   auto_user_id INTEGER NOT NULL,
   CONSTRAINT fk_auto_post_auto_user
       FOREIGN KEY (auto_user_id)
           REFERENCES auto_user(id)
           ON DELETE CASCADE
);