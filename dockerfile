FROM clojure:openjdk-11-lein

RUN apt-get -y update
RUN apt-get -y install git

WORKDIR /usr/src/app

# Copy only project.clj first to cache dependency installs
COPY project.clj /usr/src/app/

# Install dependencies based on project.clj
RUN lein deps

# By default start a REPL
CMD ["bash"]