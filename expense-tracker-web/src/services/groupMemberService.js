import axios from "axios";

const API = "https://groupexpensetracker-backend.onrender.com/api/v1/group-members";

// ✅ ADD MEMBER (SAFE)
export const addMember = async (data) => {
  if (!data || !data.groupId || !data.userId) {
    throw new Error("groupId and userId are required");
  }

  const res = await axios.post(API, {
    groupId: Number(data.groupId),
    userId: Number(data.userId)
  });

  return res;
};

// ✅ GET MEMBERS (SAFE)
export const getMembers = async (groupId) => {
  if (!groupId) {
    throw new Error("groupId is required");
  }

  const res = await axios.get(`${API}/${Number(groupId)}`);
  return res;
};