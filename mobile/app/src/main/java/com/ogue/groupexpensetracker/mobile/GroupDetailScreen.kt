package com.ogue.groupexpensetracker.mobile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ─── Brand Colors ─────────────────────────────────────────────────────────────
private val Green       = Color(0xFF26C69A)
private val GreenDark   = Color(0xFF1BAB84)
private val GreenLight  = Color(0xFFE3F8F2)
private val GreenText   = Color(0xFF0E5C47)
private val Background  = Color(0xFFF1F4F9)
private val CardBg      = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF111827)
private val TextSecond  = Color(0xFF6B7280)
private val DividerCol  = Color(0xFFE5E7EB)
private val ErrorRed    = Color(0xFFEF4444)

@Composable
fun GroupDetailScreen(
    groupId: Long,
    userId: Long,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val api     = remember { ApiClient.retrofit.create(ApiService::class.java) }

    var group          by remember { mutableStateOf<Group?>(null) }
    var members        by remember { mutableStateOf<List<GroupMemberResponse>>(emptyList()) }
    var expenses       by remember { mutableStateOf<List<Expense>>(emptyList()) }
    var isLoading      by remember { mutableStateOf(true) }
    var isCreator      by remember { mutableStateOf(false) }
    var showAddMember  by remember { mutableStateOf(false) }
    var showAddExpense by remember { mutableStateOf(false) }

    // ── Loaders ───────────────────────────────────────────────────────────────
    fun loadMembers() {
        api.getMembers(groupId).enqueue(object : Callback<List<GroupMemberResponse>> {
            override fun onResponse(
                call: Call<List<GroupMemberResponse>>,
                response: Response<List<GroupMemberResponse>>
            ) {
                if (response.isSuccessful) members = response.body() ?: emptyList()
            }
            override fun onFailure(call: Call<List<GroupMemberResponse>>, t: Throwable) {
                Toast.makeText(context, "Members error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun loadExpenses() {
        api.getExpenses(groupId).enqueue(object : Callback<List<Expense>> {
            override fun onResponse(call: Call<List<Expense>>, response: Response<List<Expense>>) {
                if (response.isSuccessful) expenses = response.body() ?: emptyList()
            }
            override fun onFailure(call: Call<List<Expense>>, t: Throwable) {
                Toast.makeText(context, "Expenses error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    LaunchedEffect(groupId) {
        api.getGroups(userId).enqueue(object : Callback<List<Group>> {
            override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                if (response.isSuccessful) {
                    val found = response.body()?.find { it.id == groupId }
                    group = found
                    // ✅ Check creator: Group.createdBy matches userId
                    // We use the members list first member heuristic if no createdBy field
                    // Will finalize after members load
                }
                isLoading = false
            }
            override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                isLoading = false
            }
        })
        loadMembers()
        loadExpenses()
    }

    // ✅ Derive isCreator once members are loaded:
    // The creator is the FIRST member added (lowest id) — matches CreateGroupScreen flow
    // where creator is immediately added as first member after group creation
    LaunchedEffect(members) {
        if (members.isNotEmpty()) {
            val firstMember = members.minByOrNull { it.id }
            isCreator = firstMember?.user?.id == userId
        }
    }

    val totalExpenses = expenses.sumOf { it.amount }

    // ── Dialogs ───────────────────────────────────────────────────────────────
    if (showAddMember) {
        AddMemberDialog(
            groupId   = groupId,
            api       = api,
            onDismiss = { showAddMember = false },
            onSuccess = {
                showAddMember = false
                loadMembers()
                Toast.makeText(context, "Member added!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showAddExpense) {
        AddExpenseDialog(
            groupId   = groupId,
            userId    = userId,
            api       = api,
            onDismiss = { showAddExpense = false },
            onSuccess = {
                showAddExpense = false
                loadExpenses()
                Toast.makeText(context, "Expense added!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // ── UI ────────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // ── TOP BAR ──────────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Green)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(GreenDark)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (isLoading) "Group Details"
                            else group?.name ?: "Group Details",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }

            // ── GROUP SUMMARY CARD ────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(GreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (group?.name?.take(1) ?: "G").uppercase(),
                                color = GreenText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                group?.name ?: "Loading...",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "${members.size} members  •  ${expenses.size} expenses",
                                color = TextSecond,
                                fontSize = 13.sp
                            )
                            if (isCreator) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "You are the creator",
                                    color = Green,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── STATS ROW ─────────────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBg),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Total Expenses", color = TextSecond, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "₱${"%.2f".format(totalExpenses)}",
                                color = Green,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBg),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Members", color = TextSecond, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "${members.size}",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── MEMBERS HEADER ────────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Members",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                    // ✅ Only creator sees Add Member button
                    if (isCreator) {
                        TextButton(onClick = { showAddMember = true }) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = Green,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Add Member",
                                color = Green,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // ── MEMBERS LIST ──────────────────────────────────────────────────
            if (members.isEmpty()) {
                item {
                    Text(
                        "No members yet",
                        color = TextSecond,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                items(members) { member ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBg),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(GreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = member.user.firstname.take(1).uppercase(),
                                    color = GreenText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "${member.user.firstname} ${member.user.lastname}",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                                Text(member.user.email, color = TextSecond, fontSize = 12.sp)
                            }
                            // ✅ Only creator sees Delete button
                            // ✅ Cannot delete yourself (the creator)
                            if (isCreator && member.user.id != userId) {
                                IconButton(
                                    onClick = {
                                        api.removeMember(member.id, userId)
                                            .enqueue(object : Callback<Void> {
                                                override fun onResponse(
                                                    call: Call<Void>,
                                                    response: Response<Void>
                                                ) {
                                                    when (response.code()) {
                                                        204 -> {
                                                            loadMembers()
                                                            Toast.makeText(context, "Member removed", Toast.LENGTH_SHORT).show()
                                                        }
                                                        403 -> Toast.makeText(context, "Only the group creator can remove members", Toast.LENGTH_SHORT).show()
                                                        else -> Toast.makeText(context, "Failed to remove member", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            })
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Remove",
                                        tint = ErrorRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            // ── EXPENSES HEADER ───────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Expenses",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                    TextButton(onClick = { showAddExpense = true }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = Green,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Add Expense",
                            color = Green,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // ── EXPENSES TABLE ────────────────────────────────────────────────
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Green)
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text("Title",   color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(2f))
                            Text("Amount",  color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                            Text("Paid By", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        }
                        if (isLoading) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) { CircularProgressIndicator(color = Green, strokeWidth = 2.dp) }
                        } else if (expenses.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) { Text("No expenses yet", color = TextSecond, fontSize = 14.sp) }
                        } else {
                            expenses.forEachIndexed { index, expense ->
                                val paidByName = members
                                    .find { it.user.id == expense.paidBy }
                                    ?.user?.firstname ?: "User ${expense.paidBy}"
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(if (index % 2 == 0) Color.White else Color(0xFFF9FAFB))
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(expense.description, color = TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(2f))
                                    Text("₱${"%.2f".format(expense.amount)}", color = Green, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                                    Text(paidByName, color = TextSecond, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                }
                                if (index < expenses.size - 1) {
                                    HorizontalDivider(color = DividerCol, thickness = 0.5.dp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// ─── Add Member Dialog ────────────────────────────────────────────────────────
// ✅ 2-step: search user by email → then add by userId (no backend changes)
@Composable
fun AddMemberDialog(
    groupId: Long,
    api: ApiService,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var email      by remember { mutableStateOf("") }
    var isLoading  by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "Add Member",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Enter the email of the user to add.",
                    color = TextSecond,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; emailError = false },
                    label = { Text("Email") },
                    placeholder = { Text("user@email.com", color = TextSecond) },
                    isError = emailError,
                    supportingText = if (emailError) {
                        { Text("Enter a valid email", color = ErrorRed, fontSize = 12.sp) }
                    } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green,
                        unfocusedBorderColor = DividerCol,
                        cursorColor = Green
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecond)
                    ) { Text("Cancel") }

                    Button(
                        onClick = {
                            if (email.isBlank() || !email.contains("@")) {
                                emailError = true
                                return@Button
                            }
                            isLoading = true

                            // ── Step 1: Find user by email ────────────────────
                            api.searchUserByEmail(email.trim())
                                .enqueue(object : Callback<UserResponse> {
                                    override fun onResponse(
                                        call: Call<UserResponse>,
                                        response: Response<UserResponse>
                                    ) {
                                        if (!response.isSuccessful || response.body() == null) {
                                            isLoading = false
                                            Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                                            return
                                        }

                                        val foundUser = response.body()!!

                                        // ── Step 2: Add member by userId ──────
                                        val request = AddMemberRequest(
                                            groupId = groupId,
                                            userId  = foundUser.id
                                        )
                                        api.addGroupMember(request)
                                            .enqueue(object : Callback<AddMemberRequest> {
                                                override fun onResponse(
                                                    call: Call<AddMemberRequest>,
                                                    response: Response<AddMemberRequest>
                                                ) {
                                                    isLoading = false
                                                    if (response.isSuccessful) {
                                                        onSuccess()
                                                    } else {
                                                        Toast.makeText(context, "User already a member", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                                override fun onFailure(
                                                    call: Call<AddMemberRequest>,
                                                    t: Throwable
                                                ) {
                                                    isLoading = false
                                                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            })
                                    }

                                    override fun onFailure(
                                        call: Call<UserResponse>,
                                        t: Throwable
                                    ) {
                                        isLoading = false
                                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                    }
                                })
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Green),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text("Add", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// ─── Add Expense Dialog ───────────────────────────────────────────────────────
@Composable
fun AddExpenseDialog(
    groupId: Long,
    userId: Long,
    api: ApiService,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var amount      by remember { mutableStateOf("") }
    var isLoading   by remember { mutableStateOf(false) }
    var descError   by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "Add Expense",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Record a new expense for this group.",
                    color = TextSecond,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it; descError = false },
                    label = { Text("Description") },
                    placeholder = { Text("e.g. Dinner, Groceries...", color = TextSecond) },
                    isError = descError,
                    supportingText = if (descError) {
                        { Text("Description is required", color = ErrorRed, fontSize = 12.sp) }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green,
                        unfocusedBorderColor = DividerCol,
                        cursorColor = Green
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it; amountError = false },
                    label = { Text("Amount") },
                    placeholder = { Text("0.00", color = TextSecond) },
                    isError = amountError,
                    supportingText = if (amountError) {
                        { Text("Enter a valid amount", color = ErrorRed, fontSize = 12.sp) }
                    } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    prefix = { Text("₱ ", color = Green, fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green,
                        unfocusedBorderColor = DividerCol,
                        cursorColor = Green
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecond)
                    ) { Text("Cancel") }

                    Button(
                        onClick = {
                            descError   = description.isBlank()
                            amountError = amount.toDoubleOrNull() == null || amount.toDouble() <= 0
                            if (descError || amountError) return@Button
                            isLoading = true
                            val expense = Expense(
                                description = description.trim(),
                                amount      = amount.toDouble(),
                                groupId     = groupId,
                                paidBy      = userId
                            )
                            api.addExpense(expense).enqueue(object : Callback<Expense> {
                                override fun onResponse(
                                    call: Call<Expense>,
                                    response: Response<Expense>
                                ) {
                                    isLoading = false
                                    if (response.isSuccessful) onSuccess()
                                    else Toast.makeText(context, "Failed to add expense", Toast.LENGTH_SHORT).show()
                                }
                                override fun onFailure(call: Call<Expense>, t: Throwable) {
                                    isLoading = false
                                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Green),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text("Add", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}