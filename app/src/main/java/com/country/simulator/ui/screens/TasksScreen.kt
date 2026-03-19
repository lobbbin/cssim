package com.country.simulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.country.simulator.model.MicroTask
import com.country.simulator.model.Priority
import com.country.simulator.model.TaskStatus
import com.country.simulator.model.TaskCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    tasks: List<MicroTask>,
    onTaskClick: (MicroTask) -> Unit,
    onTaskComplete: (MicroTask, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTask by remember { mutableStateOf<MicroTask?>(null) }
    var showTaskDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasks") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Task categories filter
            TaskFilterRow()
            
            // Tasks list with pagination support
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onClick = {
                            selectedTask = task
                            showTaskDialog = true
                        }
                    )
                }
                
                if (tasks.isEmpty()) {
                    item {
                        EmptyTasksState()
                    }
                }
            }
        }
    }
    
    // Task detail dialog
    if (showTaskDialog && selectedTask != null) {
        TaskDetailDialog(
            task = selectedTask!!,
            onDismiss = {
                showTaskDialog = false
                selectedTask = null
            },
            onComplete = { option ->
                onTaskComplete(selectedTask!!, option)
                showTaskDialog = false
                selectedTask = null
            }
        )
    }
}

@Composable
fun TaskFilterRow() {
    var selectedCategory by remember { mutableStateOf<TaskCategory?>(null) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedCategory == null,
            onClick = { selectedCategory = null },
            label = { Text("All") },
            leadingIcon = if (selectedCategory == null) {
                { Icon(Icons.Default.Done, contentDescription = null, Modifier.size(18.dp)) }
            } else null
        )
        
        FilterChip(
            selected = selectedCategory == TaskCategory.POLITICS,
            onClick = { selectedCategory = TaskCategory.POLITICS },
            label = { Text("Politics") },
            leadingIcon = if (selectedCategory == TaskCategory.POLITICS) {
                { Icon(Icons.Default.Done, contentDescription = null, Modifier.size(18.dp)) }
            } else null
        )
        
        FilterChip(
            selected = selectedCategory == TaskCategory.ECONOMY,
            onClick = { selectedCategory = TaskCategory.ECONOMY },
            label = { Text("Economy") },
            leadingIcon = if (selectedCategory == TaskCategory.ECONOMY) {
                { Icon(Icons.Default.Done, contentDescription = null, Modifier.size(18.dp)) }
            } else null
        )
        
        FilterChip(
            selected = selectedCategory == TaskCategory.DIPLOMACY,
            onClick = { selectedCategory = TaskCategory.DIPLOMACY },
            label = { Text("Diplomacy") },
            leadingIcon = if (selectedCategory == TaskCategory.DIPLOMACY) {
                { Icon(Icons.Default.Done, contentDescription = null, Modifier.size(18.dp)) }
            } else null
        )
    }
}

@Composable
fun TaskCard(
    task: MicroTask,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = getTaskCardColor(task.priority, task.status)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium
                )
                
                PriorityBadge(priority = task.priority)
            }
            
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryChip(category = task.category)
                
                if (task.dueDate != null) {
                    Text(
                        text = formatDueDate(task.dueDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun PriorityBadge(priority: Priority) {
    val (color, label) = when (priority) {
        Priority.URGENT -> Color.Red to "Urgent"
        Priority.HIGH -> Color(0xFFFF9800) to "High"
        Priority.MEDIUM -> Color(0xFF2196F3) to "Medium"
        Priority.LOW -> Color(0xFF4CAF50) to "Low"
    }
    
    AssistChip(
        onClick = { },
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = color
        )
    )
}

@Composable
fun CategoryChip(category: TaskCategory) {
    val icon = when (category) {
        TaskCategory.POLITICS -> Icons.Default.Policy
        TaskCategory.ECONOMY -> Icons.Default.Assessment
        TaskCategory.LAW -> Icons.Default.Gavel
        TaskCategory.DIPLOMACY -> Icons.Default.Public
        TaskCategory.DEFENSE -> Icons.Default.Shield
        TaskCategory.SOCIAL -> Icons.Default.People
        TaskCategory.INFRASTRUCTURE -> Icons.Default.Construction
        TaskCategory.HEALTH -> Icons.Default.Favorite
        TaskCategory.EDUCATION -> Icons.Default.School
        TaskCategory.ENVIRONMENT -> Icons.Default.Eco
    }
    
    AssistChip(
        onClick = { },
        label = { Text(category.name, style = MaterialTheme.typography.labelSmall) },
        leadingIcon = { Icon(icon, contentDescription = null, Modifier.size(16.dp)) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

@Composable
fun TaskDetailDialog(
    task: MicroTask,
    onDismiss: () -> Unit,
    onComplete: (String?) -> Unit
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(task.title) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyLarge
                )
                
                // Task details
                TaskDetailRow("Category", task.category.name)
                TaskDetailRow("Priority", task.priority.name)
                TaskDetailRow("Status", task.status.name)
                
                if (task.dueDate != null) {
                    TaskDetailRow("Due Date", formatDueDate(task.dueDate))
                }
                
                // Options if available
                if (task.options.isNotEmpty()) {
                    Text(
                        text = "Select an option:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    task.options.split(",").forEach { option ->
                        RadioOption(
                            text = option.trim(),
                            selected = selectedOption == option.trim(),
                            onClick = { selectedOption = option.trim() }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onComplete(selectedOption) },
                enabled = task.options.isEmpty() || selectedOption != null
            ) {
                Text("Complete Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun TaskDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun RadioOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun EmptyTasksState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.TaskAlt,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "No Tasks",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "All tasks completed. New tasks will appear as you play.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

@Composable
fun getTaskCardColor(priority: Priority, status: TaskStatus): Color {
    return when (status) {
        TaskStatus.COMPLETED -> MaterialTheme.colorScheme.surfaceVariant
        TaskStatus.CANCELLED, TaskStatus.EXPIRED, TaskStatus.FAILED -> 
            MaterialTheme.colorScheme.errorContainer
        else -> when (priority) {
            Priority.URGENT -> MaterialTheme.colorScheme.errorContainer
            Priority.HIGH -> MaterialTheme.colorScheme.tertiaryContainer
            else -> MaterialTheme.colorScheme.surfaceVariant
        }
    }
}

fun formatDueDate(dueDate: Long): String {
    val days = (dueDate - System.currentTimeMillis()) / (24 * 60 * 60 * 1000)
    return when {
        days < 0 -> "Overdue"
        days == 0L -> "Due today"
        days == 1L -> "Due tomorrow"
        days <= 7 -> "Due in $days days"
        else -> "Due in ${days / 7} weeks"
    }
}
