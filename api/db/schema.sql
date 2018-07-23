CREATE TABLE feeds (
    "id" serial NOT NULL PRIMARY KEY,
    "feed_url" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "site_url" TEXT NOT NULL,
    "image_url" TEXT
);
