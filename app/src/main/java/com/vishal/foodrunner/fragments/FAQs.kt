package com.vishal.foodrunner.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vishal.foodrunner.R
import com.vishal.foodrunner.adapter.FAQsAdapter
import com.vishal.foodrunner.modul.FAQS
import kotlinx.android.synthetic.main.fragment_faqs.*


class FAQs : Fragment() {
    lateinit var faqs:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    var list= listOf<FAQS>(
        FAQS("Q1. I don't remember my password.","Answer: You have already created an account but you can't remember your password? Click on 'Login/Sign Up' at the top of the page. Then click on 'Forgot Password?'. Fill out your phone number and email, otp will be sent to you by Email and fill it to create a new password."),
        FAQS("Q2. How Can I creat my account at Food Runner?","Answer: Click on 'Register or Sign up' at the bottom of the login page. Then fill out your information in the 'Create an account' section and click the 'Register' button."),
        FAQS("Q3. What are your Delivery hours?","Answer: Our delivery hour is from 10:00 AM to 08:00 PM."),
        FAQS("Q4. How much time it take to deliver the order?","Answer: Generally it takes between 45 minutes to 1 hour time to deliver the order. Due to long distance or heavy traffic, delivery might take few extra minutes."),
        FAQS("Q5. Can i order from any location?","Answer: We will deliver to any limited listed area of Kanpur Initially, we will be serving at the main Kanpur area and not to old Kanpur area."),
        FAQS("Q6. Do you Support bulk order?","Answer: In order to provide all customers with a great selection and to ensure on-time delivery of your meal, we request you to order bulk quantity at least 24 hours in advance."),
        FAQS("Q7. Can i get discount for big order?","Answer: Sorry, as a practice we do not give discounts as we work on very slim margins, already.\n" +
                "But, we could check with the Restaurant - and IF they offer any special discount, we'd be happy to pass it on to you!"),
        FAQS("Q8. How to contact us?","Answer: You can reach us by\n" +
                "Email: foodrunner@suxes.in\n" +
                "Mobile: +91-902 802 8888 / +91-20-6647 2020"),
        FAQS("Q9. Where do the rating and review comes from?","Answer: All ratings and reviews come from the foodrunner.com community, and a user can only leave a review after completing a purchase with that merchant."),
        FAQS("Q10. Do I need to pay extra to order using this site/app?",
            "Answer: Not as long as the restaurant you order from is not charging any convenience fees you don't have to pay extra.  Restaurants can choose to charge convenience. Our goal is to provide best online ordering and deliver system to restaurants and delivery services which offer the best possible experience for customers.")
    )
    lateinit var recyclerAdapter:FAQsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_faqs, container, false)
        faqs=view.findViewById(R.id.recyclerFAQs)
        layoutManager=LinearLayoutManager(activity)
        recyclerAdapter= FAQsAdapter(activity as Context,list)
        faqs.adapter=recyclerAdapter
        faqs.layoutManager=layoutManager
        faqs.addItemDecoration(DividerItemDecoration(faqs.context,(layoutManager as LinearLayoutManager).orientation))
        return view
    }


}
