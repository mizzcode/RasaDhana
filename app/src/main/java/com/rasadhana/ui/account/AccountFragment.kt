package com.rasadhana.ui.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rasadhana.R
import com.rasadhana.databinding.FragmentAccountBinding
import com.rasadhana.ui.login.LoginActivity
import com.rasadhana.ui.setting.SettingActivity
import com.rasadhana.ui.setting.SettingActivity.Companion.EXTRA_USER
import kotlinx.coroutines.launch
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

                val uri = try {
                    Uri.parse(user.photoUrl)
                } catch (e: Exception) {
                    null
                }

                if (uri != null && ("content" == uri.scheme || "file" == uri.scheme)) {
                    Log.d("accountFragment", "MASUK KE URI ACCOUNT $uri")
                    Glide.with(requireContext())
                        .load(uri)
                        .error(R.drawable.baseline_account_box_24)
                        .into(profileImage)
                } else {
                    Log.d("accountFragment", "MASUK BUKAN URI")
                    Glide.with(requireContext())
                        .load(user.photoUrl)
                        .error(R.drawable.baseline_account_box_24)
                        .into(profileImage)
                }

                binding.tvSettings.setOnClickListener {
                    Log.d("Settings User", user.name)
                    val intent = Intent(requireContext(), SettingActivity::class.java)
                    intent.putExtra(EXTRA_USER, user)
                    startActivity(intent)
                }

                tvLogout.setOnClickListener {
                    if (user.isLogin) {
                        accountViewModel.logout(requireContext())
                        return@setOnClickListener
                    }

//                  logout firebase signIn with google
                    Firebase.auth.signOut()
                    Log.d("Logout", "Firebase sign-out successful")

                    // Hapus kredensial aktif
                    clearCredentialState()

                    // Redirect ke LoginActivity
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
    }

    private fun clearCredentialState() {
        val credentialManager = CredentialManager.create(requireContext())

        // Hapus sesi kredensial
        lifecycleScope.launch {
            try {
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
                Log.d("Logout", "Credential state cleared successfully")
            } catch (e: Exception) {
                Log.e("Logout", "Failed to clear credential state: ${e.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}