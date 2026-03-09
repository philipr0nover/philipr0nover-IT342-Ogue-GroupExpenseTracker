function ExpenseRow({title,amount,date}){

  return(

    <tr>

      <td>{title}</td>

      <td>{amount}</td>

      <td>{date}</td>

    </tr>

  );

}

export default ExpenseRow;