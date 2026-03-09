function GroupCard({name,members}){

  return(

    <div className="group-card">

      <h3>{name}</h3>

      <p>{members} members</p>

      <button className="group-open-btn">
        Open Group
      </button>

    </div>

  );

}

export default GroupCard;