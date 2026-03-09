function ExpenseTable(){

  return(

    <div className="expense-table">

      <table>

        <thead>
          <tr>
            <th>Description</th>
            <th>Amount</th>
            <th>Group</th>
            <th>Date</th>
          </tr>
        </thead>

        <tbody>

          <tr>
            <td>Dinner</td>
            <td>$25</td>
            <td>Friends</td>
            <td>Today</td>
          </tr>

          <tr>
            <td>Taxi</td>
            <td>$10</td>
            <td>Roommates</td>
            <td>Yesterday</td>
          </tr>

        </tbody>

      </table>

    </div>

  );
}

export default ExpenseTable;