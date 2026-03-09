import ExpenseRow from "./ExpenseRow";

function ExpenseTable(){

  return(

    <div className="expense-table">

      <table>

        <thead>

          <tr>
            <th>Title</th>
            <th>Amount</th>
            <th>Date</th>
          </tr>

        </thead>

        <tbody>

          <ExpenseRow title="Dinner" amount="$45" date="May 10"/>
          <ExpenseRow title="Taxi" amount="$12" date="May 11"/>
          <ExpenseRow title="Groceries" amount="$30" date="May 12"/>

        </tbody>

      </table>

    </div>

  );

}

export default ExpenseTable;