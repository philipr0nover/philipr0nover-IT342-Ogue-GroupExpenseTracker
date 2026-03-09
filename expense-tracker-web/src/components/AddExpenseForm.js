import { useState } from "react";

function AddExpenseForm(){

  const [title,setTitle] = useState("");
  const [amount,setAmount] = useState("");

  const handleSubmit = (e)=>{
    e.preventDefault();

    if(title === "" || amount === ""){
      alert("Please fill all fields");
      return;
    }

    alert("Expense added: " + title);

    setTitle("");
    setAmount("");
  };

  return(

    <form className="add-expense-form" onSubmit={handleSubmit}>

      <input
        className="expense-input"
        placeholder="Expense title"
        value={title}
        onChange={(e)=>setTitle(e.target.value)}
      />

      <input
        className="expense-input"
        placeholder="Amount"
        value={amount}
        onChange={(e)=>setAmount(e.target.value)}
      />

      <button className="expense-add-btn">
        Add Expense
      </button>

    </form>

  );

}

export default AddExpenseForm;