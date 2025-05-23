package team7.inplace.place.util;

public record Button(
        String title,
        Link link
) {

    public static Button of(String buttonName, Link link) {
        return new Button(buttonName, link);
    }
}
