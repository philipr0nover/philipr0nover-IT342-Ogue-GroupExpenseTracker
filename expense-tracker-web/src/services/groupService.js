import axios from "axios";

const API = "http://localhost:8080/groups";

export const getGroups = () => {
  return axios.get(API);
};

export const createGroup = (group) => {
  return axios.post(API, group);
};