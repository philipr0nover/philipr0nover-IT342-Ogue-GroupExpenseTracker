import { useNavigate } from "react-router-dom";

function GroupCard({ id, name, members }){

  const navigate = useNavigate();

  return(
    <div className="group-card">

      <h3>{name}</h3>
      <p>{members} members</p>

      <button onClick={() => navigate(`/groups/${id}`)}>
        Open Group
      </button>

    </div>
  );
}

export default GroupCard;