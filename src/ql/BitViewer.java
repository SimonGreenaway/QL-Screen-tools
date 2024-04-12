/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ql;

import java.awt.Color;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JPanel;

/**
 *
 * @author simon
 */
public class BitViewer extends javax.swing.JFrame
{
    final File files[]=new File("/home/simon/code/ql/pengo/rom set 1").listFiles(File::isFile);
    int fileCounter=0;

    /**
     * Creates new form BitViewer
     */
    public BitViewer()
    {
        initComponents();

        fileTextField.setText(files[fileCounter].getName());
    }

    public class MyPanel extends JPanel
    {
        private final int bitsPerPixel=8;

        private Color palette4[]={new Color(0x000000),new Color(0x005500),
            new Color(0x00aa00),new Color(0x00ff00),new Color(0x0000ff),
            new Color(0x0055ff),new Color(0x00aaff),new Color(0x00ffff),
            new Color(0xff0000),new Color(0xff5500),new Color(0xffaa00),
            new Color(0xffff00),new Color(0xff00ff),new Color(0xff55ff),
            new Color(0xffaaff),new Color(0xffffff)};

        @Override public void paint(Graphics g)
        {
            g.clearRect(0,0,getWidth(),getHeight());

            int width=0;

            if(jRadioButton1.isSelected()) width=8;
            else if(jRadioButton2.isSelected()) width=12;
            else if(jRadioButton3.isSelected()) width=16;
            else if(jRadioButton4.isSelected()) width=32;

            int blockSize=getWidth()/width;

            try(final DataInputStream in=new DataInputStream(new FileInputStream(files[fileCounter])))
            {
                int x=0,y=0,page=0;

                final int showPage=((Integer)pageSpinner.getValue()).intValue();

                while(true)
                {
                    int data=in.read();
                    if(data==-1) break;

                    if(bitsPerPixel==1)
                    {
                        for(int i=0;i<8;i++)
                        {
                            if(page==showPage)
                            {
                                g.setColor(((data&(1<<i))>0)?Color.BLACK:Color.WHITE);

                                g.fillRect(x,y,x+blockSize,y+1);
                            }

                            x+=blockSize;
                        }
                    }
                    else if(bitsPerPixel==4)
                    {
                        for(int p=0;p<2;p++)
                        {
                            if(page==showPage)
                            {
                                g.setColor(palette4[data&15]);
                                g.fillRect(x,y,x+blockSize,y+16);
                            }

                            x+=blockSize;

                            data>>=4;
                        }
                    }
                    else if(bitsPerPixel==8)
                    {
                        if(page==showPage)
                        {
                            int red=data&0x7;
                            int green=(data>>3)&0x7;
                            int blue=(data>>6)&0x3;

                            g.setColor(new Color(red*32,green*32,blue*64));

                            g.fillRect(x,y,x+blockSize,y+16);
                        }

                        x+=blockSize;
                    }

                    if(x>=getWidth())
                    {
                        x=0;

                        if((y+=16)>=getHeight())
                        {
                            page++;
                            System.out.println(page);

                            y=0;
                        }
                    }
                }
            }
            catch(final IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        widthButtonGroup = new javax.swing.ButtonGroup();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        fileTextField = new javax.swing.JTextField();
        nextToggleButton = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        bitPanel = new MyPanel();
        jLabel1 = new javax.swing.JLabel();
        pageSpinner = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        widthButtonGroup.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("8");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jRadioButton1ActionPerformed(evt);
            }
        });

        widthButtonGroup.add(jRadioButton2);
        jRadioButton2.setText("12");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jRadioButton2ActionPerformed(evt);
            }
        });

        widthButtonGroup.add(jRadioButton3);
        jRadioButton3.setText("16");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jRadioButton3ActionPerformed(evt);
            }
        });

        widthButtonGroup.add(jRadioButton4);
        jRadioButton4.setText("32");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jRadioButton4ActionPerformed(evt);
            }
        });

        nextToggleButton.setText("Next");
        nextToggleButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                nextToggleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bitPanelLayout = new javax.swing.GroupLayout(bitPanel);
        bitPanel.setLayout(bitPanelLayout);
        bitPanelLayout.setHorizontalGroup(
            bitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        bitPanelLayout.setVerticalGroup(
            bitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 559, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(bitPanel);

        jLabel1.setText("Page:");

        pageSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                pageSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton3)
                .addGap(18, 18, 18)
                .addComponent(jRadioButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addComponent(fileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nextToggleButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 196, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pageSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextToggleButton)
                    .addComponent(jLabel1)
                    .addComponent(pageSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButton1ActionPerformed
    {//GEN-HEADEREND:event_jRadioButton1ActionPerformed
        ((MyPanel)bitPanel).repaint();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButton2ActionPerformed
    {//GEN-HEADEREND:event_jRadioButton2ActionPerformed
        ((MyPanel)bitPanel).repaint();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButton3ActionPerformed
    {//GEN-HEADEREND:event_jRadioButton3ActionPerformed
        ((MyPanel)bitPanel).repaint();
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButton4ActionPerformed
    {//GEN-HEADEREND:event_jRadioButton4ActionPerformed
        ((MyPanel)bitPanel).repaint();
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void nextToggleButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nextToggleButtonActionPerformed
    {//GEN-HEADEREND:event_nextToggleButtonActionPerformed
        if(++fileCounter==files.length) fileCounter=0;

        fileTextField.setText(files[fileCounter].getName());
        ((MyPanel)bitPanel).repaint();
    }//GEN-LAST:event_nextToggleButtonActionPerformed

    private void pageSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_pageSpinnerStateChanged
    {//GEN-HEADEREND:event_pageSpinnerStateChanged
        ((MyPanel)bitPanel).repaint();
    }//GEN-LAST:event_pageSpinnerStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try
        {
            for(javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(BitViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch(InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(BitViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch(IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(BitViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch(javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(BitViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new BitViewer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bitPanel;
    private javax.swing.JTextField fileTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton nextToggleButton;
    private javax.swing.JSpinner pageSpinner;
    private javax.swing.ButtonGroup widthButtonGroup;
    // End of variables declaration//GEN-END:variables
}
