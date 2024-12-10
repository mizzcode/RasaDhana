package com.rasadhana.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.rasadhana.R
import com.rasadhana.databinding.FragmentAccountBinding
import com.rasadhana.ui.setting.SettingActivity
import com.rasadhana.ui.setting.SettingActivity.Companion.EXTRA_EMAIL
import com.rasadhana.ui.setting.SettingActivity.Companion.EXTRA_NAME
import com.rasadhana.ui.setting.SettingActivity.Companion.EXTRA_PHOTO
import org.koin.android.ext.android.inject

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val accountViewModel: AccountViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel.getSession().observe(viewLifecycleOwner) { user ->

            with(binding) {
                tvUsername.text = user.name
                tvEmail.text = user.email

                Glide.with(requireContext())
                    .load(user.photo)
                    .error(R.drawable.baseline_account_box_24)
                    .into(profileImage)


                tvSettings.setOnClickListener {
                    val intent = Intent(requireContext(), SettingActivity::class.java)
                    intent.putExtra(EXTRA_PHOTO, user.photo)
                    intent.putExtra(EXTRA_NAME, user.name)
                    intent.putExtra(EXTRA_EMAIL, user.email)
                    startActivity(intent)
                }

                tvLogout.setOnClickListener { accountViewModel.logout(requireContext()) }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}