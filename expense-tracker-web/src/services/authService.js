import axios from "axios";

const API_URL = "https://groupexpensetracker-backend.onrender.com/api/v1/auth";

export const registerUser = (userData) => {
  return axios.post(`${API_URL}/register`, userData);
};

export const loginUser = (userData) => {
  return axios.post(`${API_URL}/login`, userData);
};