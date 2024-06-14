import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Main : AppCompatActivity() {

    private val tasks = mutableListOf<Task>()
    private var sortByCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainLayout = LinearLayout(this)
        mainLayout.id = R.id.mainLayout
        mainLayout.orientation = LinearLayout.VERTICAL
        setContentView(mainLayout)

        val tasksTextView = TextView(this)
        tasksTextView.text = "Tasks:"
        tasksTextView.textSize = 20f
        mainLayout.addView(tasksTextView)

        displayTasks()

        val addButton = Button(this)
        addButton.text = "Add Task"
        addButton.setOnClickListener {
            addTask()
        }
        val addButtonParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        addButtonParams.gravity = Gravity.END
        addButton.layoutParams = addButtonParams
        mainLayout.addView(addButton)

        val sortButton = Button(this)
        sortButton.text = "Sort"
        sortButton.setOnClickListener {
            sortByCompleted = !sortByCompleted
            sortTasks()
        }
        val sortButtonParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        sortButtonParams.gravity = Gravity.END
        sortButton.layoutParams = sortButtonParams
        mainLayout.addView(sortButton)

        val registerButton = Button(this)
        registerButton.text = "Register"
        registerButton.setOnClickListener {
            showRegisterDialog()
        }
        val registerButtonParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        registerButtonParams.gravity = Gravity.END
        registerButton.layoutParams = registerButtonParams
        mainLayout.addView(registerButton)
    }

    private fun displayTasks() {
        val mainLayout = findViewById<LinearLayout>(R.id.mainLayout)
        mainLayout.removeAllViews()

        val tasksTextView = TextView(this)
        tasksTextView.text = "Tasks:"
        tasksTextView.textSize = 20f
        mainLayout.addView(tasksTextView)

        val sortedTasks = if (sortByCompleted) {
            tasks.sortedBy { it.isCompleted }
        } else {
            tasks.sortedBy { !it.isCompleted }
        }

        for (task in sortedTasks) {
            val taskLayout = LinearLayout(this)
            taskLayout.orientation = LinearLayout.HORIZONTAL

            val checkBox = CheckBox(this)
            checkBox.text = task.name
            checkBox.isChecked = task.isCompleted
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
            }
            checkBox.setOnClickListener {
                editTask(task)
            }
            taskLayout.addView(checkBox)

            val reminderButton = Button(this)
            reminderButton.text = "Set Reminder"
            reminderButton.setOnClickListener {
                setReminder(task)
            }
            taskLayout.addView(reminderButton)

            mainLayout.addView(taskLayout)
        }
    }

    private fun addTask() {
        val newTask = Task("New Task ${tasks.size + 1}", false)
        tasks.add(newTask)
        displayTasks()
    }

    private fun editTask(task: Task) {
        val editText = EditText(this)
        editText.setText(task.name)

        AlertDialog.Builder(this)
            .setTitle("Edit Task")
            .setView(editText)
            .setPositiveButton("Save") { dialog, _ ->
                task.name = editText.text.toString()
                displayTasks()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setReminder(task: Task) {
        showToast("Reminder set for: ${task.name}")
    }

    private fun sortTasks() {
        displayTasks()
    }

    private fun showRegisterDialog() {
        val usernameEditText = EditText(this)
        val passwordEditText = EditText(this)
        passwordEditText.inputType = EditText.TYPE_CLASS_TEXT or EditText.TYPE_TEXT_VARIATION_PASSWORD

        val dialogView = LinearLayout(this)
        dialogView.orientation = LinearLayout.VERTICAL
        dialogView.addView(usernameEditText)
        dialogView.addView(passwordEditText)

        AlertDialog.Builder(this)
            .setTitle("Register")
            .setMessage("Enter username and password:")
            .setView(dialogView)
            .setPositiveButton("Register") { dialog, _ ->
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()
                showToast("Registered with username: $username")
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showToast(message: String) {
        println(message)
    }

    data class Task(var name: String, var isCompleted: Boolean)
}
