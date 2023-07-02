# Start with a base image that has the necessary build tools and libraries
FROM gcc

# Set the working directory to /app
WORKDIR /app

# Copy the source code into the container
COPY src/ /app

# Compile the application using g++
RUN g++ -o pacman main.cpp

# Set the command to run the application
CMD ["./pacman"]

#docker build -t oxdman -f E:\git\PacmanPP\DockerFile .