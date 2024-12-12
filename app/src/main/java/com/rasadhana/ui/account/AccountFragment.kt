package com.rasadhana.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.rasadhana.R
import com.rasadhana.databinding.FragmentAccountBinding
import com.rasadhana.ui.setting.SettingActivity
import com.rasadhana.ui.setting.SettingActivity.Companion.EXTRA_USER
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

    override fun onResume() {
        super.onResume()

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
                    intent.putExtra(EXTRA_USER, user)
                    startActivity(intent)
                }

                tvLogout.setOnClickListener { accountViewModel.logout(requireContext()) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}