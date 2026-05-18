import axios from "axios";

const API = "http://localhost:8080/group-members";

export const addMember = (data) => axios.post(API, data);
export const getMembers = (groupId) => axios.get(`${API}/${groupId}`);