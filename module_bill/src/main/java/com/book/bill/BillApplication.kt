package com.book.bill

import android.app.Application
import com.book.base.constant.ApplicationPriority
import com.book.base.utils.KVUtils
import com.book.bean.BillCategory
import com.book.bill.data.BillDataBase
import io.github.prototypez.appjoint.core.ModuleSpec
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ModuleSpec(priority = ApplicationPriority.MODULE_BOOK_SHELF_PRIORITY)
class BillApplication : Application() {

    companion object {
        private const val IS_INIT_DATABASE = "is_init_database"
    }

    override fun onCreate() {
        super.onCreate()
        initDataBase()

    }

    private fun initDataBase() {
        BillDataBase.init(this)

        if (KVUtils.getBoolean(IS_INIT_DATABASE)) {
            return
        }

        GlobalScope.launch {
            mutableListOf<BillCategory>().apply {
                var sort = 1

                arrayOf(
                    arrayOf(getString(R.string.category_catering), "icon_catering"),
                    arrayOf(getString(R.string.category_shopping), "icon_shopping"),
                    arrayOf(getString(R.string.category_commodity), "icon_commodity"),
                    arrayOf(getString(R.string.category_traffic), "icon_traffic"),
                    arrayOf(getString(R.string.category_vegetable), "icon_vegetable"),
                    arrayOf(getString(R.string.category_fruits), "icon_fruits"),
                    arrayOf(getString(R.string.category_snack), "icon_snack"),
                    arrayOf(getString(R.string.category_sport), "icon_sport"),
                    arrayOf(getString(R.string.category_entertainmente), "icon_entertainmente"),
                    arrayOf(getString(R.string.category_communicate), "icon_communicate"),
                    arrayOf(getString(R.string.category_dress), "icon_dress"),
                    arrayOf(getString(R.string.category_beauty), "icon_beauty"),
                    arrayOf(getString(R.string.category_house), "icon_house"),
                    arrayOf(getString(R.string.category_home), "icon_home"),
                    arrayOf(getString(R.string.category_child), "icon_child"),
                    arrayOf(getString(R.string.category_elder), "icon_elder"),
                    arrayOf(getString(R.string.category_social), "icon_social"),
                    arrayOf(getString(R.string.category_travel), "icon_travel"),
                    arrayOf(getString(R.string.category_smoke), "icon_smoke"),
                    arrayOf(getString(R.string.category_digital), "icon_digital"),
                    arrayOf(getString(R.string.category_car), "icon_car"),
                    arrayOf(getString(R.string.category_medical), "icon_medical"),
                    arrayOf(getString(R.string.category_books), "icon_books"),
                    arrayOf(getString(R.string.category_study), "icon_study"),
                    arrayOf(getString(R.string.category_pet), "icon_pet"),
                    arrayOf(getString(R.string.category_money), "icon_money"),
                    arrayOf(getString(R.string.category_gift), "icon_gift"),
                    arrayOf(getString(R.string.category_office), "icon_office"),
                    arrayOf(getString(R.string.category_repair), "icon_repair"),
                    arrayOf(getString(R.string.category_donate), "icon_donate"),
                    arrayOf(getString(R.string.category_lottery), "icon_lottery"),
                    arrayOf(getString(R.string.category_friend), "icon_friend"),
                    arrayOf(getString(R.string.category_express), "icon_express")
                ).forEach {
                    add(BillCategory(it[0], it[1], BillCategory.GROUP_EXPEND, sort))
                    sort++
                }

                sort = 1
                arrayOf(
                    arrayOf(getString(R.string.category_wages), "icon_wage"),
                    arrayOf(getString(R.string.category_part_time), "icon_parttimework"),
                    arrayOf(getString(R.string.category_shares), "icon_finance"),
                    arrayOf(getString(R.string.category_cash_gift), "icon_money_2"),
                    arrayOf(getString(R.string.category_other), "icon_other_income")
                ).forEach {
                    add(BillCategory(it[0], it[1], BillCategory.GROUP_INCOME, sort))
                    sort++
                }

                BillDataBase.getInstance().getCategoryDao().insertAll(this)
            }
            KVUtils.putBoolean(IS_INIT_DATABASE, true)
        }
    }
}