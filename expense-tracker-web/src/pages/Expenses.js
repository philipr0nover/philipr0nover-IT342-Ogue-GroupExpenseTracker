import Sidebar from "../components/Sidebar";
import HeaderBar from "../components/HeaderBar";
import AddExpenseForm from "../components/AddExpenseForm";
import ExpenseTable from "../components/ExpenseTable";

function Expenses(){

  return(

    <div className="dashboard-container">

      <Sidebar/>

      <div className="dashboard-main">

        <HeaderBar title="Expenses"/>

        <AddExpenseForm/>

        <ExpenseTable/>

      </div>

    </div>

  );

}

export default Expenses;