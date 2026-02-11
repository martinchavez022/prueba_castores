# Use a Node.js base image
FROM node:18-alpine

WORKDIR /app

# Copy package.json and install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of the source code
COPY . .

# Expose the port the development server runs on
EXPOSE 3000

# Start the development server
CMD ["npm", "start"]
