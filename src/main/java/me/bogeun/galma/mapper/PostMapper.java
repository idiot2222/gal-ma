package me.bogeun.galma.mapper;

import me.bogeun.galma.entity.Post;
import me.bogeun.galma.payload.PostWriteForm;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PostMapper {

    PostWriteForm entityToWriteForm(Post post);

}
