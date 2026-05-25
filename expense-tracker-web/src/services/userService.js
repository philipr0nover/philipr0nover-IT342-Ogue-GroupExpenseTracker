import axios from "axios";

const API = "https://groupexpensetracker-backend.onrender.com/api/v1/users";

export const getUsers = () => axios.get(API);