package net.oschina.app.fragment;

import java.util.ArrayList;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.adapter.NotebookAdapter;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.bean.NotebookData;
import net.oschina.app.bean.SimpleBackPage;
import net.oschina.app.db.NoteDatabase;
import net.oschina.app.util.KJAnimations;
import net.oschina.app.util.UIHelper;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 便签列表界面
 * 
 * @author kymjs(kymjs123@gmail.com)
 * 
 */
public class NoteBookFragment extends BaseFragment implements
        OnItemClickListener, OnItemLongClickListener, OnRefreshListener {

    @InjectView(R.id.frag_note_list)
    GridView mList;
    @InjectView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.frag_note_menu)
    RelativeLayout mMenu;
    @InjectView(R.id.frag_note_img_del)
    ImageView mImgDel;
    @InjectView(R.id.frag_note_img_upload)
    ImageView mImgRefresh;
    @InjectView(R.id.frag_note_temp)
    ImageView mMenuBtn;

    private NoteDatabase noteDb;
    private ArrayList<NotebookData> datas;
    private NotebookAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_note, container,
                false);
        ButterKnife.inject(this, rootView);
        initData();
        initView(rootView);
        return rootView;
    }

    @Override
    public void initData() {
        noteDb = new NoteDatabase(getActivity());
        datas = noteDb.query();
        if (datas != null) {
            adapter = new NotebookAdapter(getActivity(), datas);
        }
    }

    @Override
    public void initView(View view) {
        mList.setAdapter(adapter);
        mList.setOnItemLongClickListener(this);
        mList.setOnItemClickListener(this);
        mImgDel.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
    }

    @Override
    public void onResume() {
        super.onResume();
        refurbish();
        if (!AppContext.getInstance().isLogin()) {
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    /**
     * 刷新列表数据
     */
    private void refurbish() {
        datas = noteDb.query();
        if (datas != null && adapter != null) {
            adapter.refurbishData(datas);
        }
    }

    /**
     * 长按显示功能操作菜单
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id) {
        if (!adapter.isShowCheckBox()) {
            adapter.showCheckBox();
            mSwipeRefreshLayout.setEnabled(false);
            mMenu.setVisibility(View.VISIBLE);
            KJAnimations.openAnimation(mMenu, mMenuBtn, 500);
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        if (adapter.isShowCheckBox()) {
            datas.get(position).setChecked(!datas.get(position).isChecked());
            adapter.refurbishData(datas);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(NoteEditFragment.NOTE_FROMWHERE_KEY,
                    NoteEditFragment.NOTEBOOK_ITEM);
            bundle.putSerializable(NoteEditFragment.NOTE_KEY,
                    datas.get(position));
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.NOTE_EDIT,
                    bundle);
        }
    }

    /**
     * 删除选中项
     */
    private void delete() {
        for (NotebookData data : datas) {
            if (data.isChecked()) {
                noteDb.delete(data.getId());
            }
        }
    }

    /**
     * 同步选中项
     */
    private void upload() {
        for (NotebookData data : datas) {
            if (data.isChecked()) {
                noteDb.delete(data.getId());
            }
        }
    }

    /**
     * 功能菜单操作完成后的界面恢复工作
     */
    private void finishHandle() {
        refurbish();
        adapter.hideCheckBox();
        KJAnimations.closeAnimation(mMenu, mMenuBtn, 500);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pub_tweet_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.public_menu_send:
            Bundle bundle = new Bundle();
            bundle.putInt(NoteEditFragment.NOTE_FROMWHERE_KEY,
                    NoteEditFragment.NOTEBOOK_FRAGMENT);
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.NOTE_EDIT,
                    bundle);
            break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.frag_note_img_del:
            delete();
            finishHandle();
            break;
        case R.id.frag_note_img_upload:
            upload();
            finishHandle();
            break;
        }
    }

    @Override
    public void onRefresh() {
        if (mState == STATE_REFRESH) {
            return;
        }

        // 设置顶部正在刷新
        mList.setSelection(0);
        setSwipeRefreshLoadingState();

        /* !!! 设置耗时操作 !!! */

        refurbish();
        setSwipeRefreshLoadedState();
    }

    /** 设置顶部正在加载的状态 */
    private void setSwipeRefreshLoadingState() {
        mState = STATE_REFRESH;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            // 防止多次重复刷新
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    /** 设置顶部加载完毕的状态 */
    private void setSwipeRefreshLoadedState() {
        mState = STATE_NOMORE;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

}