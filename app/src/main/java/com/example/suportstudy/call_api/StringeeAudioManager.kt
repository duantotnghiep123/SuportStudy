package com.example.suportstudy.call_api

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.os.Build
import android.util.Log
import org.webrtc.ThreadUtils
import java.util.*

object StringeeAudioManager {
    private const val TAG = "StringeeAudioManager"
    private const val SPEAKERPHONE_AUTO = "auto"
    private const val SPEAKERPHONE_TRUE = "true"
    private const val SPEAKERPHONE_FALSE = "false"

    /**
     * AudioDevice is the names of possible audio devices that we currently
     * support.
     */
    enum class AudioDevice {
        SPEAKER_PHONE, WIRED_HEADSET, EARPIECE, BLUETOOTH, NONE
    }

    /**
     * AudioManager state.
     */
    enum class AudioManagerState {
        UNINITIALIZED, PREINITIALIZED, RUNNING
    }

    /**
     * Selected audio device change event.
     */
    interface AudioManagerEvents {
        // Callback fired once audio device is changed or list of available audio devices changed.
        fun onAudioDeviceChanged(
            selectedAudioDevice: AudioDevice?, availableAudioDevices: Set<AudioDevice>?
        )
    }

    private var context: Context? = null
    private var audioManager: AudioManager? = null

    private var audioManagerEvents: AudioManagerEvents? = null
    private var amState: AudioManagerState? = null
    private var savedAudioMode = AudioManager.MODE_INVALID
    private var savedIsSpeakerPhoneOn = false
    private var savedIsMicrophoneMute = false
    private var hasWiredHeadset = false

    // Default audio device; speaker phone for video calls or earpiece for audio
    // only calls.
    private var defaultAudioDevice: AudioDevice? = null

    // Contains the currently selected audio device.
    // This device is changed automatically using a certain scheme where e.g.
    // a wired headset "wins" over speaker phone. It is also possible for a
    // user to explicitly select a device (and overrid any predefined scheme).
    // See |userSelectedAudioDevice| for details.
    private var selectedAudioDevice: AudioDevice? = null

    // Contains the user-selected audio device which overrides the predefined
    // selection scheme.
    // explicit selection based on choice by userSelectedAudioDevice.
    private var userSelectedAudioDevice: AudioDevice? = null

    // Contains speakerphone setting: auto, true or false
    private val useSpeakerphone: String? = SPEAKERPHONE_AUTO

    // Proximity sensor object. It measures the proximity of an object in cm
    // relative to the view screen of a device and can therefore be used to
    // assist device switching (close to ear <=> use headset earpiece if
    // available, far from ear <=> use speaker phone).
//    @Nullable
//    private StringeeProximitySensor proximitySensor;

    // Proximity sensor object. It measures the proximity of an object in cm
    // relative to the view screen of a device and can therefore be used to
    // assist device switching (close to ear <=> use headset earpiece if
    // available, far from ear <=> use speaker phone).
    //    @Nullable
    //    private StringeeProximitySensor proximitySensor;
    // Handles all tasks related to Bluetooth headset devices.
    private var bluetoothManager: StringeeBluetoothManager? = null

    // Contains a list of available audio devices. A Set collection is used to
    // avoid duplicate elements.
    private var audioDevices: MutableSet<AudioDevice> = HashSet()

    // Broadcast receiver for wired headset intent broadcasts.
    private var wiredHeadsetReceiver: BroadcastReceiver? = null

    // Callback method for changes in audio focus.
    private var audioFocusChangeListener: OnAudioFocusChangeListener? = null

    /**
     * This method is called when the proximity sensor reports a state change,
     * e.g. from "NEAR to FAR" or from "FAR to NEAR".
     */
    private fun onProximitySensorChangedState() {
        if (useSpeakerphone != SPEAKERPHONE_AUTO) {
            return
        }

        // The proximity sensor should only be activated when there are exactly two
        // available audio devices.
//        if (audioDevices.size() == 2 && audioDevices.contains(AudioDevice.EARPIECE)
//                && audioDevices.contains(AudioDevice.SPEAKER_PHONE)) {
//            if (proximitySensor.sensorReportsNearState()) {
//                // Sensor reports that a "handset is being held up to a person's ear",
//                // or "something is covering the light sensor".
//                setAudioDeviceInternal(AudioDevice.EARPIECE);
//            } else {
//                // Sensor reports that a "handset is removed from a person's ear", or
//                // "the light sensor is no longer covered".
//                setAudioDeviceInternal(AudioDevice.SPEAKER_PHONE);
//            }
//        }
    }

    /* Receiver which handles changes in wired headset availability. */
    private class WiredHeadsetReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val state = intent.getIntExtra("state", STATE_UNPLUGGED)
            val microphone = intent.getIntExtra("microphone", HAS_NO_MIC)
            val name = intent.getStringExtra("name")
            hasWiredHeadset = state == STATE_PLUGGED
            updateAudioDeviceState()
        }

        companion object {
            private const val STATE_UNPLUGGED = 0
            private const val STATE_PLUGGED = 1
            private const val HAS_NO_MIC = 0
            private const val HAS_MIC = 1
        }
    }

    /**
     * Construction.
     */
    fun create(context: Context): StringeeAudioManager? {
        return create(context)
    }

    private fun StringeeAudioManager(mcontext: Context) {
        ThreadUtils.checkIsOnMainThread()
        context = mcontext
        audioManager = mcontext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        bluetoothManager = StringeeBluetoothManager.create(mcontext, this)
        wiredHeadsetReceiver = WiredHeadsetReceiver()
        amState = AudioManagerState.UNINITIALIZED
        defaultAudioDevice = if (useSpeakerphone == SPEAKERPHONE_FALSE) {
            AudioDevice.EARPIECE
        } else {
            AudioDevice.SPEAKER_PHONE
        }

        // Create and initialize the proximity sensor.
        // Note that, the sensor will not be active until start() has been called.
//        proximitySensor = StringeeProximitySensor.create(context,
//                // This method will be called each time a state change is detected.
//                // Example: user holds their hand over the device (closer than ~5 cm),
//                // or removes their hand from the device.
//                this::onProximitySensorChangedState);
        Log.d(TAG, "defaultAudioDevice: $defaultAudioDevice")
    }

    @SuppressLint("WrongConstant")
    fun start(audioManagerEvents: AudioManagerEvents?) {
        ThreadUtils.checkIsOnMainThread()
        if (amState == AudioManagerState.RUNNING) {
            Log.e(TAG, "AudioManager is already active")
            return
        }
        Log.d(TAG, "AudioManager starts...")
        this.audioManagerEvents = audioManagerEvents
        amState = AudioManagerState.RUNNING

        // Store current audio state so we can restore it when stop() is called.
        savedAudioMode = audioManager!!.mode
        savedIsSpeakerPhoneOn = audioManager!!.isSpeakerphoneOn
        savedIsMicrophoneMute = audioManager!!.isMicrophoneMute
        hasWiredHeadset = hasWiredHeadset()

        // Create an AudioManager.OnAudioFocusChangeListener instance.
        audioFocusChangeListener = OnAudioFocusChangeListener { focusChange ->
            val typeOfChange: String
            typeOfChange = when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> "AUDIOFOCUS_GAIN"
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT -> "AUDIOFOCUS_GAIN_TRANSIENT"
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE -> "AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE"
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK -> "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK"
                AudioManager.AUDIOFOCUS_LOSS -> "AUDIOFOCUS_LOSS"
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> "AUDIOFOCUS_LOSS_TRANSIENT"
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK"
                else -> "AUDIOFOCUS_INVALID"
            }
            Log.d(TAG, "onAudioFocusChange: $typeOfChange")
        }

        // Request audio playout focus (without ducking) and install listener for changes in focus.
        val result = audioManager!!.requestAudioFocus(
            audioFocusChangeListener,
            AudioManager.STREAM_VOICE_CALL, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
        )
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d(TAG, "Audio focus request granted for VOICE_CALL streams")
        } else {
            Log.e(TAG, "Audio focus request failed")
        }

        // Start by setting MODE_IN_COMMUNICATION as default audio mode. It is
        // required to be in this mode when playout and/or recording starts for
        // best possible VoIP performance.
        audioManager!!.mode = AudioManager.MODE_IN_CALL

        // Always disable microphone mute during a WebRTC call.
        setMicrophoneMute(false)

        // Set initial device states.
        userSelectedAudioDevice = AudioDevice.NONE
        selectedAudioDevice = AudioDevice.NONE
        audioDevices.clear()

        // Initialize and start Bluetooth if a BT device is available or initiate
        // detection of new (enabled) BT devices.
        bluetoothManager!!.start()

        // Activate proximity sensor
//        if (isVideoCall) {
//            proximitySensor.start();
//        }

        // Do initial selection of audio device. This setting can later be changed
        // either by adding/removing a BT or wired headset or by covering/uncovering
        // the proximity sensor.
        updateAudioDeviceState()

        // Register receiver for broadcast intents related to adding/removing a
        // wired headset.
        registerReceiver(wiredHeadsetReceiver, IntentFilter(Intent.ACTION_HEADSET_PLUG))
        Log.d(TAG, "AudioManager started")
    }

    @SuppressLint("WrongConstant")
    fun stop() {
        Log.d(TAG, "stop")
        ThreadUtils.checkIsOnMainThread()
        if (amState != AudioManagerState.RUNNING) {
            Log.e(TAG, "Trying to stop AudioManager in incorrect state: $amState")
            return
        }
        amState = AudioManagerState.UNINITIALIZED
        unregisterReceiver(wiredHeadsetReceiver)
        bluetoothManager!!.stop()

        // Restore previously stored audio states.
        setSpeakerphoneOn(savedIsSpeakerPhoneOn)
        setMicrophoneMute(savedIsMicrophoneMute)
        audioManager!!.mode = savedAudioMode

        // Abandon audio focus. Gives the previous focus owner, if any, focus.
        audioManager!!.abandonAudioFocus(audioFocusChangeListener)
        audioFocusChangeListener = null
        Log.d(TAG, "Abandoned audio focus for VOICE_CALL streams")

//        if (proximitySensor != null) {
//            proximitySensor.stop();
//            proximitySensor = null;
//        }
        audioManagerEvents = null
        Log.d(TAG, "AudioManager stopped")
    }

    /**
     * Changes selection of the currently active audio device.
     */
    private fun setAudioDeviceInternal(device: AudioDevice?) {
        Log.d(TAG, "setAudioDeviceInternal(device=$device)")
        when (device) {
            AudioDevice.SPEAKER_PHONE -> setSpeakerphoneOn(true)
            AudioDevice.EARPIECE -> setSpeakerphoneOn(false)
            AudioDevice.WIRED_HEADSET -> setSpeakerphoneOn(false)
            AudioDevice.BLUETOOTH -> setSpeakerphoneOn(false)
            else -> Log.e(TAG, "Invalid audio device selection")
        }
        selectedAudioDevice = device
    }


    fun setDefaultAudioDevice(defaultDevice: AudioDevice?) {
        ThreadUtils.checkIsOnMainThread()
        when (defaultDevice) {
            AudioDevice.SPEAKER_PHONE -> defaultAudioDevice = defaultDevice
            AudioDevice.EARPIECE -> defaultAudioDevice = if (hasEarpiece()) {
                defaultDevice
            } else {
                AudioDevice.SPEAKER_PHONE
            }
            else -> Log.e(TAG, "Invalid default audio device selection")
        }
        Log.d(TAG, "setDefaultAudioDevice(device=$defaultAudioDevice)")
        updateAudioDeviceState()
    }

    /**
     * Changes selection of the currently active audio device.
     */
    fun selectAudioDevice(device: AudioDevice) {
        ThreadUtils.checkIsOnMainThread()
        if (!audioDevices.contains(device)) {
            Log.e(TAG, "Can not select $device from available $audioDevices")
        }
        userSelectedAudioDevice = device
        updateAudioDeviceState()
    }


    fun getAudioDevices(): Set<AudioDevice?>? {
        ThreadUtils.checkIsOnMainThread()
        return Collections.unmodifiableSet(HashSet(audioDevices))
    }


    fun getSelectedAudioDevice(): AudioDevice? {
        ThreadUtils.checkIsOnMainThread()
        return selectedAudioDevice
    }


    private fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter) {
        context!!.registerReceiver(receiver, filter)
    }

    /**
     * Helper method for unregistration of an existing receiver.
     */
    private fun unregisterReceiver(receiver: BroadcastReceiver?) {
        context!!.unregisterReceiver(receiver)
    }

    /**
     * Sets the speaker phone mode.
     */
    fun setSpeakerphoneOn(on: Boolean) {
        val wasOn = audioManager!!.isSpeakerphoneOn
        if (wasOn == on) {
            return
        }
        audioManager!!.isSpeakerphoneOn = on
    }


    private fun setMicrophoneMute(on: Boolean) {
        val wasMuted = audioManager!!.isMicrophoneMute
        if (wasMuted == on) {
            return
        }
        audioManager!!.isMicrophoneMute = on
    }


    private fun hasEarpiece(): Boolean {
        return context!!.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
    }


    @SuppressLint("WrongConstant")
    @Deprecated("")
    private fun hasWiredHeadset(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            audioManager!!.isWiredHeadsetOn
        } else {
            val devices = audioManager!!.getDevices(AudioManager.GET_DEVICES_ALL)
            for (device in devices) {
                val type = device.type
                if (type == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                    Log.d(TAG, "hasWiredHeadset: found wired headset")
                    return true
                } else if (type == AudioDeviceInfo.TYPE_USB_DEVICE) {
                    Log.d(TAG, "hasWiredHeadset: found USB audio device")
                    return true
                }
            }
            false
        }
    }

    /**
     * Updates list of possible audio devices and make new device selection.
     */
    fun updateAudioDeviceState() {
        ThreadUtils.checkIsOnMainThread()
        Log.d(
            TAG, "--- updateAudioDeviceState: "
                    + "wired headset=" + hasWiredHeadset + ", "
                    + "BT state=" + bluetoothManager!!.getState()
        )
        Log.d(
            TAG, "Device status: "
                    + "available=" + audioDevices + ", "
                    + "selected=" + selectedAudioDevice + ", "
                    + "user selected=" + userSelectedAudioDevice
        )

        // Check if any Bluetooth headset is connected. The internal BT state will
        // change accordingly.
        if (bluetoothManager!!.getState() === StringeeBluetoothManager.State.HEADSET_AVAILABLE || bluetoothManager!!.getState() === StringeeBluetoothManager.State.HEADSET_UNAVAILABLE || bluetoothManager!!.getState() === StringeeBluetoothManager.State.SCO_DISCONNECTING) {
            bluetoothManager!!.updateDevice()
        }

        // Update the set of available audio devices.
        val newAudioDevices: MutableSet<AudioDevice> = HashSet()
        if (bluetoothManager!!.getState() === StringeeBluetoothManager.State.SCO_CONNECTED || bluetoothManager!!.getState() === StringeeBluetoothManager.State.SCO_CONNECTING || bluetoothManager!!.getState() === StringeeBluetoothManager.State.HEADSET_AVAILABLE) {
            newAudioDevices.add(AudioDevice.BLUETOOTH)
        }
        if (hasWiredHeadset) {
            // If a wired headset is connected, then it is the only possible option.
            newAudioDevices.add(AudioDevice.WIRED_HEADSET)
        } else {
            // No wired headset, hence the audio-device list can contain speaker
            // phone (on a tablet), or speaker phone and earpiece (on mobile phone).
            newAudioDevices.add(AudioDevice.SPEAKER_PHONE)
            if (hasEarpiece()) {
                newAudioDevices.add(AudioDevice.EARPIECE)
            }
        }
        // Store state which is set to true if the device list has changed.
        var audioDeviceSetUpdated = audioDevices != newAudioDevices
        // Update the existing audio device set.
        audioDevices = newAudioDevices
        // Correct user selected audio devices if needed.
        if (bluetoothManager!!.getState() === StringeeBluetoothManager.State.HEADSET_UNAVAILABLE
            && userSelectedAudioDevice == AudioDevice.BLUETOOTH
        ) {
            // If BT is not available, it can't be the user selection.
            userSelectedAudioDevice = AudioDevice.NONE
        }
        if (hasWiredHeadset && userSelectedAudioDevice == AudioDevice.SPEAKER_PHONE) {
            // If user selected speaker phone, but then plugged wired headset then make
            // wired headset as user selected device.
            userSelectedAudioDevice = AudioDevice.WIRED_HEADSET
        }
        if (!hasWiredHeadset && userSelectedAudioDevice == AudioDevice.WIRED_HEADSET) {
            // If user selected wired headset, but then unplugged wired headset then make
            // speaker phone as user selected device.
            userSelectedAudioDevice = AudioDevice.SPEAKER_PHONE
        }

        // Need to start Bluetooth if it is available and user either selected it explicitly or
        // user did not select any output device.
        val needBluetoothAudioStart =
            (bluetoothManager!!.getState() === StringeeBluetoothManager.State.HEADSET_AVAILABLE
                    && (userSelectedAudioDevice == AudioDevice.NONE
                    || userSelectedAudioDevice == AudioDevice.BLUETOOTH))

        // Need to stop Bluetooth audio if user selected different device and
        // Bluetooth SCO connection is established or in the process.
        val needBluetoothAudioStop =
            ((bluetoothManager!!.getState() === StringeeBluetoothManager.State.SCO_CONNECTED
                    || bluetoothManager!!.getState() === StringeeBluetoothManager.State.SCO_CONNECTING)
                    && (userSelectedAudioDevice != AudioDevice.NONE
                    && userSelectedAudioDevice != AudioDevice.BLUETOOTH))
        if (bluetoothManager!!.getState() === StringeeBluetoothManager.State.HEADSET_AVAILABLE || bluetoothManager!!.getState() === StringeeBluetoothManager.State.SCO_CONNECTING || bluetoothManager!!.getState() === StringeeBluetoothManager.State.SCO_CONNECTED) {
            Log.d(
                TAG, "Need BT audio: start=" + needBluetoothAudioStart + ", "
                        + "stop=" + needBluetoothAudioStop + ", "
                        + "BT state=" + bluetoothManager!!.getState()
            )
        }

        // Start or stop Bluetooth SCO connection given states set earlier.
        if (needBluetoothAudioStop) {
            bluetoothManager!!.stopScoAudio()
            bluetoothManager!!.updateDevice()
        }
        if (needBluetoothAudioStart && !needBluetoothAudioStop) {
            // Attempt to start Bluetooth SCO audio (takes a few second to start).
            if (!bluetoothManager!!.startScoAudio()) {
                // Remove BLUETOOTH from list of available devices since SCO failed.
                audioDevices.remove(AudioDevice.BLUETOOTH)
                audioDeviceSetUpdated = true
            }
        }

        // Update selected audio device.
        val newAudioDevice: AudioDevice?
        newAudioDevice =
            if (bluetoothManager!!.getState() === StringeeBluetoothManager.State.SCO_CONNECTED) {
                // If a Bluetooth is connected, then it should be used as output audio
                // device. Note that it is not sufficient that a headset is available;
                // an active SCO channel must also be up and running.
                AudioDevice.BLUETOOTH
            } else if (hasWiredHeadset) {
                // If a wired headset is connected, but Bluetooth is not, then wired headset is used as
                // audio device.
                AudioDevice.WIRED_HEADSET
            } else {
                // No wired headset and no Bluetooth, hence the audio-device list can contain speaker
                // phone (on a tablet), or speaker phone and earpiece (on mobile phone).
                // |defaultAudioDevice| contains either AudioDevice.SPEAKER_PHONE or AudioDevice.EARPIECE
                // depending on the user's selection.
                defaultAudioDevice
            }
        // Switch to new device but only if there has been any changes.
        if (newAudioDevice != selectedAudioDevice || audioDeviceSetUpdated) {
            // Do the required device switch.
            setAudioDeviceInternal(newAudioDevice)
            Log.d(
                TAG, "New device status: "
                        + "available=" + audioDevices + ", "
                        + "selected=" + newAudioDevice
            )
            if (audioManagerEvents != null) {
                // Notify a listening client that audio device has been changed.
                audioManagerEvents!!.onAudioDeviceChanged(selectedAudioDevice, audioDevices)
            }
        }
        Log.d(TAG, "--- updateAudioDeviceState done")
    }
}