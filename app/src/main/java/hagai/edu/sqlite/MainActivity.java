package hagai.edu.sqlite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hagai.edu.sqlite.dialogs.AddTodoFragment;
import hagai.edu.sqlite.models.Todo;
import hagai.edu.sqlite.sqlite.DAO;


public class MainActivity extends AppCompatActivity {
    List<Todo> todos;
    TodosAdapter adapter;
    RecyclerView rvTodos;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTodo();
            }
        });

        todos = DAO.getInstance(this).getTodos();

        //adapter
        adapter = new TodosAdapter(this, todos);
        //findViewById recycler
        rvTodos = (RecyclerView) findViewById(R.id.rvTodos);
        //set the adapter
        rvTodos.setAdapter(adapter);
        //set the layout manager
        rvTodos.setLayoutManager(new LinearLayoutManager(this));
//        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//            }
//        }, new IntentFilter("addedTodo"));
    }

    BroadcastReceiver todosInsertedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Todo todo = intent.getParcelableExtra("todo");
            todos.add(todo);
            adapter.notifyItemInserted(todos.size() - 1);
        }
    };

    BroadcastReceiver todoUpdatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Todo todo = intent.getParcelableExtra("todo");
            for (int i = 0; i < todos.size(); i++) {
                if (todos.get(i).getId() == todo.getId()){
                    todos.set(i, todo);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    };

    BroadcastReceiver todoDeletedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Todo todo = intent.getParcelableExtra("todo");
            for (int i = 0; i < todos.size(); i++) {
                if (todos.get(i).getId() == todo.getId()){
                    todos.remove(i);
                    adapter.notifyItemRemoved(i);
                    break;
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(this);
        //import static ness.edu.sqlite.dialogs.AddTodoFragment.INTENT_ACTION_INSERT;
        IntentFilter insertFilter = new IntentFilter(AddTodoFragment.INTENT_ACTION_INSERT);
        IntentFilter updateFilter = new IntentFilter(AddTodoFragment.INTENT_ACTION_UPDATE);
        IntentFilter deleteFilter = new IntentFilter(AddTodoFragment.INTENT_ACTION_DELETE);

        mgr.registerReceiver(todosInsertedReceiver, insertFilter);
        mgr.registerReceiver(todoUpdatedReceiver, updateFilter);
        mgr.registerReceiver(todoDeletedReceiver, deleteFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(this);
        mgr.unregisterReceiver(todosInsertedReceiver);
        mgr.unregisterReceiver(todoUpdatedReceiver);
        mgr.unregisterReceiver(todoDeletedReceiver);
    }

    private void addTodo() {
        AddTodoFragment addTodoFragment =
                AddTodoFragment.newInstance(AddTodoFragment.ACTION_INSERT, null);

        addTodoFragment.show(getSupportFragmentManager(), "todoDialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.TodoViewHolder> {
        //properties:
        private List<Todo> data;
        AppCompatActivity activity;

        //Constructor:
        public TodosAdapter(AppCompatActivity activity, List<Todo> data) {
            this.activity = activity;
            this.data = data;
        }
        @Override
        public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //need inflater-> context
            View itemView = LayoutInflater.from(activity).inflate(R.layout.todo_item, parent, false);
            TodoViewHolder holder = new TodoViewHolder(itemView);
            holder.activity = activity;
            return holder;
        }
        @Override
        public void onBindViewHolder(TodoViewHolder holder, int position) {
            //need  data.get(position)
            Todo todo = data.get(position);
            holder.tvImportance.setText(todo.getImportance());
            holder.tvMission.setText(todo.getMission());
            holder.model = todo;
        }
        @Override
        public int getItemCount() {
            //need data.size()
            return data.size();
        }
        //find views by id, cache the view finding here.
        static class TodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tvMission;
            TextView tvImportance;
            Todo model;
            FloatingActionButton fabDelete;
            AppCompatActivity activity;

            public TodoViewHolder(View v) {
                super(v);
                tvImportance = v.findViewById(R.id.tvImportance);
                tvMission = v.findViewById(R.id.tvMission);
                fabDelete = v.findViewById(R.id.fabDelete);
                v.setOnClickListener(this);
                fabDelete.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (view == fabDelete){
                    DAO.getInstance(activity).deleteTodo(model.getId());

                    Intent intent = new Intent(AddTodoFragment.INTENT_ACTION_DELETE);
                    intent.putExtra("todo", model); //parcelable.
                    LocalBroadcastManager.getInstance(activity).
                            sendBroadcast(intent);
                }else {
                    //MainActivity a = (MainActivity) view.getContext();
                    AddTodoFragment dialog = AddTodoFragment.newInstance(AddTodoFragment.ACTION_UPDATE, model);
                    dialog.show(activity.getSupportFragmentManager(), "updateDialog");
                }
            }
        }
    }
}