FROM clojure:openjdk-11-lein

WORKDIR /usr/src/app

# Copy only project.clj first to cache dependency installs
COPY project.clj /usr/src/app/

# Install dependencies based on project.clj
RUN lein deps

# By default start a REPL
CMD ["lein", "repl"]
