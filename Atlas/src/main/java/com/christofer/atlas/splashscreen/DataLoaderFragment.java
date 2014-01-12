package com.christofer.atlas.splashscreen;


import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

/**
 *
 */
public class DataLoaderFragment extends Fragment {

    /**
     * Interface to be implemented by classes that want to get notified of loading progress
     * or completion of loading.
     */
    public interface ProgressListener{

        /**
         * Notifies that the task has completed.
         * @param result
         */
        public void onCompletion(Double result);

        /**
         * Notifies the progress of loading.
         * @param value
         */
        public void onProgressUpdate(int value);
    }

    private ProgressListener progressListener;
    private Double dataResult = Double.NaN;
    private LoadingTask task;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Keep this Fragment around even during config changes
        setRetainInstance(true);
    }

    /**
     * @return  the result or NaN (Not a Number).
     */
    public Double getResult(){
        return dataResult;
    }

    /**
     * Returns true if a result has already been calculated
     *
     * @return true if a result has already been calculated
     * @see #getResult()
     */
    public boolean hasResult() {
        return !Double.isNaN(dataResult);
    }

    /**
     * Removes the ProgressListener
     *
     * @see #setProgressListener(ProgressListener)
     */
    public void removeProgressListener() {
        progressListener = null;
    }

    /**
     * Sets the ProgressListener to be notified of updates
     *
     * @param listener ProgressListener to notify
     * @see #removeProgressListener()
     */
    public void setProgressListener(ProgressListener listener) {
        progressListener = listener;
    }

    public void startLoading(){
        task = new LoadingTask();
        task.execute();
    }

    private class LoadingTask extends AsyncTask<Void, Integer, Double> {

        @Override
        protected Double doInBackground(Void... params) {
            double result = 0;
            for(int i = 0; i < 100; i++){
                try{
                    result += Math.sqrt(i);
                    Thread.sleep(50);
                    this.publishProgress(i);
                }catch(InterruptedException e){
                    return null;
                }
            }
            return Double.valueOf(result);
        }

        @Override
        protected void onPostExecute(Double result) {
            dataResult = result;
            task = null;
            if(progressListener != null){
                progressListener.onCompletion(result);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(progressListener != null){
                progressListener.onProgressUpdate(values[0]);
            }
        }
    }
}
