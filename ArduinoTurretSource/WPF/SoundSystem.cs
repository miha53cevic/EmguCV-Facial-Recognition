using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using System.Media;
using System.IO;

namespace EmguCV_WPF_Test
{
    class SoundSystem
    {
        public SoundSystem()
        {
            m_UseSoundSystem = true;
        }

        public void UseSoundSystem(bool useSoundSystem)
        {
            m_UseSoundSystem = useSoundSystem;
        }

        // Starting up sound
        public void Deploy()
        {
            string deploySound = "Portal2/Deploy/turret_deploy_" + rand.Next(1, 6).ToString() + ".wav";
            PlaySound(deploySound);
        }

        public void Detected()
        {
            string detectedSound = "Portal2/Detected/turret_active_" + rand.Next(1, 8).ToString() + ".wav";
            PlaySound(detectedSound);
        }

        public void Search()
        {
            string searchingSound = "Portal2/Searching/turret_autosearch_" + rand.Next(1, 4).ToString() + ".wav";
            PlaySound(searchingSound);
        }

        // Play sound located in soundFile
        private bool PlaySound(string soundFile)
        {
            // Check if file path is valid and return false if it is not
            if (File.Exists(soundFile) && m_UseSoundSystem)
            {
                m_soundPlayer = new SoundPlayer(soundFile);
                m_soundPlayer.Play();
            }
            else return false;

            return true;
        }

        // Wheather soundSystem is enabled or disabled
        private bool m_UseSoundSystem;

        // Create random with random tick
        private Random rand = new Random(Environment.TickCount);

        // System.Media.SoundPlayer
        private SoundPlayer m_soundPlayer;
    }
}
