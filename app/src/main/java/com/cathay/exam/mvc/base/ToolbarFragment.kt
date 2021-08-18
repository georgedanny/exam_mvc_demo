package com.cathay.exam.mvc.base


import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import com.cathay.exam.mvc.R



abstract class ToolbarFragment : BaseFragmnet() {
    private lateinit var toolbar:Toolbar
    private lateinit var ivBack:ImageView
    private lateinit var tvTitle:TextView
    private var backClick : (() -> Unit)? = null

    fun setBackPress( click:() -> Unit){
        this.backClick = click
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = initContentView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.view_toolbar,container,false)
        val content:FrameLayout = rootView.findViewById(R.id.content)
        content.addView(view)

        toolbar = rootView.findViewById(R.id.toolbar)
        ivBack = rootView.findViewById(R.id.ivBack)
        tvTitle = rootView.findViewById(R.id.title)

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                if (backClick == null){
                    nav().navigateUp()
                }else{
                    backClick?.invoke()
                }

                return@OnKeyListener true
            }
            false
        })
        return rootView
    }

    abstract fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    @CallSuper
    override fun initView() {
        val activity = context as AppCompatActivity
        activity.setSupportActionBar(toolbar)
    }

    @CallSuper
    override fun listen() {
        ivBack.setOnClickListener {
            NavHostFragment.findNavController(this).navigateUp()
        }
    }

    fun setNavigationHomeClickListen(click: (View) -> Unit) {
        ivBack.setOnClickListener {
            click.invoke(it)
        }
    }

    fun hideBackArrow() {
        ivBack.visibility = View.GONE
    }

    fun setToolbarTitle(title: String?) {
        tvTitle.text = title
    }

    fun getToolbar():Toolbar = toolbar



}