package drawrite.booknet.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import drawrite.booknet.entity.Book;
import drawrite.booknet.entity.Mentions;
import drawrite.booknet.repository.BookRepository;
import drawrite.booknet.repository.MentionsRepository;

public class MentionsViewModel extends AndroidViewModel {
    private MentionsRepository repository;
    private LiveData<List<Mentions>> allMentions;


    public MentionsViewModel(@NonNull Application application) {
        super(application);
        repository = new MentionsRepository(application);
        allMentions = repository.getAllMentions();
        if(allMentions.getValue()==null){ // check if there is data in the table
            Log.d("MentionsViewModel", "1103 no data in the mentions table  " );

        }
    }

    public void insert(Mentions mentions){
        repository.insert(mentions);
    }

    public void update(Mentions mentions){
        repository.update(mentions);
    }

    public void delete(Mentions mentions){
        repository.delete(mentions);
    }

    public void deleteAllMentions(){
        repository.deleteAllMentions();
    }

    public LiveData<List<Mentions>> getAllMentions() {
        return allMentions;
    }

    //TODO : implement remaining methods here.
    //public LiveData<List<Mentions>> getMentionsByBookIds(Integer mainBookPrimaryId){ return repository.getMentionsByBookIds(mainBookPrimaryId);}

    //public LiveData<List<String>> getBookId(String olId) { return repository.getBookId(olId); }
}
