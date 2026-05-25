import axios from "axios";

const API = "https://groupexpensetracker-backend.onrender.com/api/v1/groups";

// GET GROUPS BY USER
export const getGroups = async (userId) => {
  if (!userId) {
    throw new Error("User ID is required");
  }
  const res = await axios.get(`${API}/user/${userId}`);
  return res;
};

// ✅ UPDATED: now sends createdBy so backend knows who owns the group
export const createGroup = async (group) => {
  if (!group || !group.name) {
    throw new Error("Group name is required");
  }
  if (!group.createdBy) {
    throw new Error("createdBy is required");
  }
  const res = await axios.post(API, group);
  return res;
};