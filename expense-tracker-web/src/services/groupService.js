import axios from "axios";

const API = "http://localhost:8080/api/v1/groups";

// ✅ GET GROUPS BY USER (SAFE)
export const getGroups = async (userId) => {
  if (!userId) {
    throw new Error("User ID is required");
  }

  const res = await axios.get(`${API}/user/${userId}`);
  return res;
};

// ✅ CREATE GROUP (SAFE)
export const createGroup = async (group) => {
  if (!group || !group.name) {
    throw new Error("Group name is required");
  }

  const res = await axios.post(API, group);
  return res;
};