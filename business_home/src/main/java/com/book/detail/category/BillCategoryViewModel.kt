package com.book.detail.category

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.book.base.mvi.BaseViewModel
import com.book.bean.BillCategory
import com.book.detail.R
import com.book.detail.bean.BillCategoryDecorator
import com.book.router.IBookBillService
import io.github.prototypez.appjoint.AppJoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillCategoryViewModel :
    BaseViewModel<BillCategoryState, BillCategoryEvent, BillCategoryAction>() {
    override fun handleAction(action: BillCategoryAction) {
        when (action) {
            is BillCategoryAction.RequestIncomeList -> {
                requestIncomeList(action)
            }

            is BillCategoryAction.RequestExpendList -> {
                requestExpendList(action)
            }

            is BillCategoryAction.SaveBill -> {
                saveBill(action)
            }

            is BillCategoryAction.RemoveCategory -> {
                removeCategory(action)
            }

            is BillCategoryAction.GetAllCategoryList -> {
                getAllCategoryList(action)
            }

            is BillCategoryAction.SwapCategorySort -> {
                swapCategorySort(action)
            }

            is BillCategoryAction.SaveCategory -> {
                saveCategory(action)
            }

            is BillCategoryAction.DeleteBill -> {
                deleteBill(action)
            }

            is BillCategoryAction.EditBill -> {
                editBill(action)
            }

            is BillCategoryAction.QueryBill -> {
                queryBill(action)
            }
        }
    }

    private fun queryBill(action: BillCategoryAction.QueryBill) {
        viewModelScope.launch(Dispatchers.Main) {
            sendEvent(BillCategoryEvent.ShowLoading)

            val result = AppJoint.service(IBookBillService::class.java).queryBill(action.id)

            setState(
                BillCategoryState(
                    action = action,
                    bill = result,
                    isSuccess = true
                )
            )

            sendEvent(BillCategoryEvent.DismissLoading)
        }
    }

    private fun editBill(action: BillCategoryAction.EditBill) {
        viewModelScope.launch(Dispatchers.Main) {
            sendEvent(BillCategoryEvent.ShowLoading)

            AppJoint.service(IBookBillService::class.java).editBill(action.bill)

            setState(
                BillCategoryState(
                    action = action,
                    isSuccess = true
                )
            )

            sendEvent(BillCategoryEvent.DismissLoading)
        }
    }

    private fun deleteBill(action: BillCategoryAction.DeleteBill) {
        viewModelScope.launch(Dispatchers.Main) {
            sendEvent(BillCategoryEvent.ShowLoading)

            AppJoint.service(IBookBillService::class.java).deleteBill(action.bill)

            setState(
                BillCategoryState(
                    action = action,
                    isSuccess = true
                )
            )

            sendEvent(BillCategoryEvent.DismissLoading)
        }
    }

    private fun requestIncomeList(action: BillCategoryAction.RequestIncomeList) {
        viewModelScope.launch(Dispatchers.Main) {
            sendEvent(BillCategoryEvent.ShowLoading)

            val list = mutableListOf<BillCategoryDecorator>()
            AppJoint.service(IBookBillService::class.java).getIncomeList().forEach {
                list.add(BillCategoryDecorator(category = it))
            }

            setState(
                BillCategoryState(
                    action = action,
                    categoryList = list,
                    isSuccess = true
                )
            )

            sendEvent(BillCategoryEvent.DismissLoading)
        }
    }

    private fun requestExpendList(action: BillCategoryAction.RequestExpendList) {
        viewModelScope.launch(Dispatchers.Main) {
            sendEvent(BillCategoryEvent.ShowLoading)

            val list = mutableListOf<BillCategoryDecorator>()
            AppJoint.service(IBookBillService::class.java).getExpendList().forEach {
                list.add(BillCategoryDecorator(category = it))
            }

            setState(
                BillCategoryState(
                    action = action,
                    categoryList = list,
                    isSuccess = true
                )
            )

            sendEvent(BillCategoryEvent.DismissLoading)
        }
    }

    private fun saveBill(action: BillCategoryAction.SaveBill) {
        viewModelScope.launch(Dispatchers.Main) {
            sendEvent(BillCategoryEvent.ShowLoading)

            AppJoint.service(IBookBillService::class.java).addBill(action.bill)

            setState(
                BillCategoryState(
                    action = action,
                    isSuccess = true
                )
            )

            sendEvent(BillCategoryEvent.DismissLoading)
        }
    }

    private fun removeCategory(action: BillCategoryAction.RemoveCategory) {
        viewModelScope.launch(Dispatchers.Main) {
            sendEvent(BillCategoryEvent.ShowLoading)

            AppJoint.service(IBookBillService::class.java).removeBillCategory(action.item.category)

            setState(
                BillCategoryState(
                    action = action,
                    isSuccess = true
                )
            )

            sendEvent(BillCategoryEvent.DismissLoading)
        }
    }

    private fun getAllCategoryList(action: BillCategoryAction.GetAllCategoryList) {
        viewModelScope.launch(Dispatchers.Main) {
            sendEvent(BillCategoryEvent.ShowLoading)

            val response = withContext(Dispatchers.IO) {
                getAllCategoryMap(action.context)
            }

            setState(
                BillCategoryState(
                    action = action,
                    categoryMap = response,
                    isSuccess = true
                )
            )

            sendEvent(BillCategoryEvent.DismissLoading)
        }
    }

    private fun getAllCategoryMap(context: Context): Map<String, List<String>> {
        val categoryMap = mutableMapOf<String, List<String>>()

        mutableListOf<String>().apply {
            add("icon_entertainment_game")
            add("icon_entertainment_ping_pong")
            add("icon_entertainment_swimming")
            add("icon_entertainment_chess")
            add("icon_entertainment_whirligig")
            add("icon_climbing")
            add("icon_archery")
            add("icon_entertainment_poker")
            add("icon_entertainment_basketball")
            add("icon_entertainment_roller")
            add("icon_entertainment_badminton")
            add("icon_entertainment_racing")
            add("icon_billiards")
            add("icon_entertainment_sailing")
            add("icon_entertainment_movies")
            add("icon_entertainment_gambling")
            add("icon_entertainment_bowling")
            categoryMap[context.getString(R.string.category_entertainment_title)] = this
        }

        mutableListOf<String>().apply {
            add("icon_catering_ice_lolly")
            add("icon_catering_banana")
            add("icon_catering_chicken")
            add("icon_catering_apple")
            add("icon_catering_sushi")
            add("icon_catering_noodle")
            add("icon_catering_beer")
            add("icon_catering_bottle")
            add("icon_catering_drumstick")
            add("icon_catering_birthday_cake")
            add("icon_catering_rice")
            add("icon_catering_skewer")
            add("icon_catering_tea")
            add("icon_red_wine")
            add("icon_hot_pot")
            add("icon_hamburg")
            add("icon_catering_seafood")
            add("icon_catering_ice_cream")
            add("icon_catering_coffee")
            categoryMap[context.getString(R.string.category_catering_title)] = this
        }

        mutableListOf<String>().apply {
            add("icon_medical_ct")
            add("icon_medical_injection")
            add("icon_medical_wheelchair")
            add("icon_medical_transfusion")
            add("icon_medical_doctor")
            add("icon_medical_echometer")
            add("icon_medical_pregnant")
            add("icon_medical_medicine")
            add("icon_medical_tooth")
            add("icon_medical_child")
            categoryMap[context.getString(R.string.category_medical_title)] = this
        }

        mutableListOf<String>().apply {
            add("icon_study_school")
            add("icon_study_lamp")
            add("icon_study_blackboard")
            add("icon_study_guitars")
            add("icon_study_calculator")
            add("icon_study_penruler")
            add("icon_study_book")
            add("icon_study_hat")
            add("icon_study_piano")
            add("icon_study_penpaper")
            categoryMap[context.getString(R.string.category_study_title)] = this
        }

        mutableListOf<String>().apply {
            add("icon_traffic_charge")
            add("icon_traffic_plane")
            add("icon_traffic_bike")
            add("icon_traffic_expressway")
            add("icon_traffic_taxi")
            add("icon_traffic_refuel")
            add("icon_traffic_parking")
            add("icon_traffic_truck")
            add("icon_traffic_motorbike")
            add("icon_traffic_car")
            add("icon_traffic_double_deck_bus")
            add("icon_traffic_car_wash")
            add("icon_traffic_gasoline")
            add("icon_traffic_car_insurance")
            add("icon_traffic_train")
            add("icon_traffic_ship")
            categoryMap[context.getString(R.string.category_traffic_title)] = this
        }

        mutableListOf<String>().apply {
            add("icon_shopping_kettle")
            add("icon_shopping_baby")
            add("icon_shopping_ring")
            add("icon_shopping_glasses")
            add("icon_shopping_high_heels")
            add("icon_shopping_watch")
            add("icon_shopping_camera")
            add("icon_shopping_necklace")
            add("icon_shopping_sneaker")
            add("icon_shopping_boots")
            add("icon_shopping_flowerpot")
            add("icon_shopping_belt")
            add("icon_shopping_skirt")
            add("icon_shopping_knickers")
            add("icon_shopping_bikini")
            add("icon_shopping_headset")
            add("icon_shopping_tie")
            add("icon_shopping_trousers")
            add("icon_shopping_necktie")
            add("icon_shopping_hat")
            add("icon_shopping_toiletries")
            add("icon_shopping_lipstick")
            add("icon_shopping_cosmetics")
            add("icon_shopping_mascara")
            add("icon_shopping_flower")
            add("icon_shopping_package")
            add("icon_shopping_eye_shadow")
            add("icon_shopping_shopping_trolley")
            add("icon_shopping_earrings")
            add("icon_shopping_hand_cream")
            categoryMap[context.getString(R.string.category_shopping_title)] = this
        }

        mutableListOf<String>().apply {
            add("icon_life_moods_of_love")
            add("icon_life_hotel")
            add("icon_life_bath")
            add("icon_life_buddha")
            add("icon_life_candlelight")
            add("icon_life_sunbath")
            add("icon_life_tent")
            add("icon_life_tea")
            add("icon_life_trip")
            add("icon_life_date")
            add("icon_life_spa")
            add("icon_life_holiday")
            categoryMap[context.getString(R.string.category_life_title)] = this
        }

        mutableListOf<String>().apply {
            add("icon_personal_handshake")
            add("icon_personal_marry")
            add("icon_personal_bill")
            add("icon_personal_money")
            add("icon_personal_friend")
            add("icon_personal_pc")
            add("icon_personal_phone")
            add("icon_personal_love")
            add("icon_personal_clap")
            add("icon_personal_facial")
            add("icon_personal_favourite")
            categoryMap[context.getString(R.string.category_personal_title)] = this
        }

        mutableListOf<String>().apply {
            add("icon_home_bathtub")
            add("icon_home_renovate")
            add("icon_home_washing_machine")
            add("icon_home_tools")
            add("icon_home_water")
            add("icon_home_bed")
            add("icon_home_sofa")
            add("icon_home_air_conditioner")
            add("icon_home_wardrobe")
            add("icon_home_bread_machine")
            add("icon_home_microwave_oven")
            add("icon_home_bulb")
            add("icon_home_w_and_e")
            add("icon_home_hair_drier")
            add("icon_home_refrigerator")
            categoryMap[context.getString(R.string.category_home_title)] = this
        }

        mutableListOf<String>().apply {
            add("icon_family_pet_food")
            add("icon_family_baby_carriage")
            add("icon_family_toy_duck")
            add("icon_family_teddy_bear")
            add("icon_family_feeding_bottle")
            add("icon_family_pet_home")
            add("icon_family_baby")
            add("icon_family_dog")
            add("icon_family_nipple")
            add("icon_family_wooden_horse")
            categoryMap[context.getString(R.string.category_family_title)] = this
        }

        mutableListOf<String>().apply {
            add("icon_fitness_skating")
            add("icon_fitness_treadmills")
            add("icon_fitness_barbell")
            add("icon_fitness_fitball")
            add("icon_fitness_elliptical_machine")
            add("icon_fitness_bodybuilding")
            add("icon_fitness_barbell")
            add("icon_fitness_running")
            add("icon_fitness_boxing")
            add("icon_fitness_sit_in")
            add("icon_fitness_dumbbell")
            add("icon_fitness_hand_muscle_developer")
            categoryMap[context.getString(R.string.category_fitness_title)] = this
        }

        mutableListOf<String>().apply {
            add("icon_office_mouse")
            add("icon_office_computer")
            add("icon_office_clip")
            add("icon_office_keyboard")
            add("icon_office_desk")
            add("icon_office_pen_container")
            add("icon_office_printer")
            add("icon_office_pen_ruler")
            categoryMap[context.getString(R.string.category_office_title)] = this
        }

        mutableListOf<String>().apply {
            add("icon_income_1")
            add("icon_income_2")
            add("icon_income_3")
            add("icon_income_4")
            add("icon_income_5")
            add("icon_income_6")
            add("icon_income_7")
            add("icon_income_8")
            add("icon_income_9")
            add("icon_income_10")
            categoryMap[context.getString(R.string.income)] = this
        }

        mutableListOf<String>().apply {
            add("icon_other_diamond")
            add("icon_other_memorial_day")
            add("icon_other_flag")
            add("icon_other_crown")
            add("icon_other_zongzi")
            add("icon_other_lantern")
            add("icon_other_firecracker")
            categoryMap[context.getString(R.string.category_other)] = this
        }

        return categoryMap
    }

    private fun swapCategorySort(action: BillCategoryAction.SwapCategorySort) {
        viewModelScope.launch(Dispatchers.Main) {
            AppJoint.service(IBookBillService::class.java).swapCategorySort(action.items)
        }
    }

    private fun saveCategory(action: BillCategoryAction.SaveCategory) {
        viewModelScope.launch(Dispatchers.Main) {
            sendEvent(BillCategoryEvent.ShowLoading)

            BillCategory(name = action.name, icon = action.icon, group = action.group).apply {
                AppJoint.service(IBookBillService::class.java).addCategory(this)
            }

            setState(
                BillCategoryState(
                    action = action,
                    isSuccess = true
                )
            )

            sendEvent(BillCategoryEvent.DismissLoading)
        }
    }
}