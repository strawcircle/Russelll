package tw.com.chainsea.bruce_example;

import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import tw.com.chainsea.bruce.TabHostActivity;

public class SecondActivity extends TabHostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Map<String, TabsInfo> tabsInfoMap() {
        Map<String, TabsInfo> map = new HashMap<>();
        map.put("message", new TabsInfo("message", R.drawable.icon_message, MessageFragment.class,
                R.drawable.vertical_menu_icon, R.drawable.contact_all, R.drawable.contact_group, new OnTitleBarListener() {

            @Override
            public void onRightClick() {
                showPopupMenu();
            }

            @Override
            public void onFirstClick() {
                setCenterText("全部");
            }

            @Override
            public void onSecondClick() {
                setCenterText("群聊");
            }
        }));
        map.put("contact", new TabsInfo("contact", R.drawable.icon_contact, ContactFragment.class,
                R.drawable.vertical_menu_icon, new OnTitleBarListener() {

            @Override
            public void onRightClick() {

            }

            @Override
            public void onFirstClick() {

            }

            @Override
            public void onSecondClick() {

            }
        }));
        return map;
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(SecondActivity.this, getRightView());
        popupMenu.getMenuInflater()
                .inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.special_topic:
                        Toast.makeText(SecondActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.elite:
                        Toast.makeText(SecondActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
