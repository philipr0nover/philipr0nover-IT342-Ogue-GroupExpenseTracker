import axios from "axios";

const API = "http://localhost:8080/users";

export const getUsers = () => axios.get(API);