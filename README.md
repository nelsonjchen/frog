# frog

A media acquisition framework designed to be as flexible as possible.

The name comes from how data leapfrogs from stage to stage. 

For instance,
We could start with your favorite movie having finished downloading
on a seedbox somewhere, and a "service" in frog is responsible for watching
this seedbox. When it notices a new file has finished downloading, frog
creates a "ticket", which is then pushed out to a ftp downloader. Once
complete, the ftp downloader forwards a new ticket to have the file classified
as a movie, a tv show, or audio using another stage, which then has the
file appropriately transcoded and added to your media library. This is the dream.


## Usage

Don't, for now.

## License

Copyright © 2012 Evan Zalys

Distributed under the Eclipse Public License, the same as Clojure.
