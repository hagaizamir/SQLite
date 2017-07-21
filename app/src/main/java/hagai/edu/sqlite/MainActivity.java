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

    BroadcastReceiver todosUpdatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //get the new Todo from the intent
            Todo todo = intent.getParcelableExtra("todo");
            todos.add(todo);
            adapter.notifyItemInserted(todos.size() - 1);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(this);
        IntentFilter actionFilter = new IntentFilter("addedTodo");
        mgr.registerReceiver(todosUpdatedReceiver, actionFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(this);
        mgr.unregisterReceiver(todosUpdatedReceiver);
    }

    private void addTodo() {
        new AddTodoFragment().show(getSupportFragmentManager(), "todo");
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
        private Context context;
        private List<Todo> data;
        //Constructor:
        public TodosAdapter(Context context, List<Todo> data) {
            this.context = context;
            this.data = data;
        }
        @Override
        public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //need inflater-> context
            View itemView = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
            return new TodoViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(TodoViewHolder holder, int position) {
            //need  data.get(position)
            Todo todo = data.get(position);
            holder.tvImportance.setText(todo.getImportance());
            holder.tvMission.setText(todo.getMission());
        }
        @Override
        public int getItemCount() {
            //need data.size()
            return data.size();
        }
        //find views by id, cache the view finding here.
        static class TodoViewHolder extends RecyclerView.ViewHolder {
            TextView tvMission;
            TextView tvImportance;
            Todo model;

            public TodoViewHolder(View v) {
                super(v);
                tvImportance = v.findViewById(R.id.tvImportance);
                tvMission = v.findViewById(R.id.tvMission);

            }
        }
    }
}