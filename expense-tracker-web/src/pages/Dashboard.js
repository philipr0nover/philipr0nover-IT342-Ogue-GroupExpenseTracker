import Sidebar from "../components/Sidebar";
import HeaderBar from "../components/HeaderBar";
import StatCard from "../components/StatCard";
import ExpenseTable from "../components/ExpenseTable";
import ActivityPanel from "../components/ActivityPanel";

function Dashboard(){

  return(

    <div className="dashboard-container">

      <Sidebar/>

      <div className="dashboard-main">

        <HeaderBar/>

        <div className="stats-grid">

          <StatCard title="Total Expenses" value="$0"/>
          <StatCard title="Groups" value="0"/>
          <StatCard title="Pending Payments" value="$0"/>

        </div>

        <div className="dashboard-content">

          <ExpenseTable/>

          <ActivityPanel/>

        </div>

      </div>

    </div>

  );
}

export default Dashboard;